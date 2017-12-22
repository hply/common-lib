package com.gogoal.common.common;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.util.HashMap;

/**
 * @author wangjd on 2017/12/22 10:57.
 * @description :${annotated}.
 */
public class FileUtils {

    private static HashMap<String, String> contentTypeMap;

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
        }
        return null;
    }

    /**
     * 根据扩展名获取ContentType类型
     *
     * @param extensionName 扩展名——"apk"
     */
    public static String getContentType(String extensionName) {
        return contentTypeMap.get(extensionName);
    }

    /**
     *  常用"文件扩展名—MIME类型"匹配表。
     *  注意，此表并不全，也并不是唯一的，就像有人喜欢用浏览器打开TXT一样，你可以根据自己的爱好自定义。
     */
    static {
        contentTypeMap = new HashMap<>();
        contentTypeMap.put("3gp", "video/3gpp");
        contentTypeMap.put("aab", "application/x-authoware-bin");
        contentTypeMap.put("aam", "application/x-authoware-map");
        contentTypeMap.put("aas", "application/x-authoware-seg");
        contentTypeMap.put("ai", "application/postscript");
        contentTypeMap.put("aif", "audio/x-aiff");
        contentTypeMap.put("aifc", "audio/x-aiff");
        contentTypeMap.put("aiff", "audio/x-aiff");
        contentTypeMap.put("als", "audio/X-Alpha5");
        contentTypeMap.put("amc", "application/x-mpeg");
        contentTypeMap.put("ani", "application/octet-stream");
        contentTypeMap.put("apk", "application/vnd.android.package-archive");
        contentTypeMap.put("asc", "text/plain");
        contentTypeMap.put("asd", "application/astound");
        contentTypeMap.put("asf", "video/x-ms-asf");
        contentTypeMap.put("asn", "application/astound");
        contentTypeMap.put("asp", "application/x-asap");
        contentTypeMap.put("asx", "video/x-ms-asf");
        contentTypeMap.put("au", "audio/basic");
        contentTypeMap.put("avb", "application/octet-stream");
        contentTypeMap.put("avi", "video/x-msvideo");
        contentTypeMap.put("awb", "audio/amr-wb");
        contentTypeMap.put("bcpio", "application/x-bcpio");
        contentTypeMap.put("bin", "application/octet-stream");
        contentTypeMap.put("bld", "application/bld");
        contentTypeMap.put("bld2", "application/bld2");
        contentTypeMap.put("bmp", "image/bmp");
        contentTypeMap.put("bpk", "application/octet-stream");
        contentTypeMap.put("bz2", "application/x-bzip2");
        contentTypeMap.put("cal", "image/x-cals");
        contentTypeMap.put("ccn", "application/x-cnc");
        contentTypeMap.put("cco", "application/x-cocoa");
        contentTypeMap.put("cdf", "application/x-netcdf");
        contentTypeMap.put("cgi", "magnus-internal/cgi");
        contentTypeMap.put("chat", "application/x-chat");
        contentTypeMap.put("class", "application/octet-stream");
        contentTypeMap.put("clp", "application/x-msclip");
        contentTypeMap.put("cmx", "application/x-cmx");
        contentTypeMap.put("co", "application/x-cult3d-object");
        contentTypeMap.put("cod", "image/cis-cod");
        contentTypeMap.put("cpio", "application/x-cpio");
        contentTypeMap.put("cpt", "application/mac-compactpro");
        contentTypeMap.put("crd", "application/x-mscardfile");
        contentTypeMap.put("csh", "application/x-csh");
        contentTypeMap.put("csm", "chemical/x-csml");
        contentTypeMap.put("csml", "chemical/x-csml");
        contentTypeMap.put("css", "text/css");
        contentTypeMap.put("cur", "application/octet-stream");
        contentTypeMap.put("dcm", "x-lml/x-evm");
        contentTypeMap.put("dcr", "application/x-director");
        contentTypeMap.put("dcx", "image/x-dcx");
        contentTypeMap.put("dhtml", "text/html");
        contentTypeMap.put("dir", "application/x-director");
        contentTypeMap.put("dll", "application/octet-stream");
        contentTypeMap.put("dmg", "application/octet-stream");
        contentTypeMap.put("dms", "application/octet-stream");
        contentTypeMap.put("doc", "application/msword");
        contentTypeMap.put("dot", "application/x-dot");
        contentTypeMap.put("dvi", "application/x-dvi");
        contentTypeMap.put("dwf", "drawing/x-dwf");
        contentTypeMap.put("dwg", "application/x-autocad");
        contentTypeMap.put("dxf", "application/x-autocad");
        contentTypeMap.put("dxr", "application/x-director");
        contentTypeMap.put("ebk", "application/x-expandedbook");
        contentTypeMap.put("emb", "chemical/x-embl-dl-nucleotide");
        contentTypeMap.put("embl", "chemical/x-embl-dl-nucleotide");
        contentTypeMap.put("eps", "application/postscript");
        contentTypeMap.put("eri", "image/x-eri");
        contentTypeMap.put("es", "audio/echospeech");
        contentTypeMap.put("esl", "audio/echospeech");
        contentTypeMap.put("etc", "application/x-earthtime");
        contentTypeMap.put("etx", "text/x-setext");
        contentTypeMap.put("evm", "x-lml/x-evm");
        contentTypeMap.put("evy", "application/x-envoy");
        contentTypeMap.put("exe", "application/octet-stream");
        contentTypeMap.put("fh4", "image/x-freehand");
        contentTypeMap.put("fh5", "image/x-freehand");
        contentTypeMap.put("fhc", "image/x-freehand");
        contentTypeMap.put("fif", "image/fif");
        contentTypeMap.put("fm", "application/x-maker");
        contentTypeMap.put("fpx", "image/x-fpx");
        contentTypeMap.put("fvi", "video/isivideo");
        contentTypeMap.put("gau", "chemical/x-gaussian-input");
        contentTypeMap.put("gca", "application/x-gca-compressed");
        contentTypeMap.put("gdb", "x-lml/x-gdb");
        contentTypeMap.put("gif", "image/gif");
        contentTypeMap.put("gps", "application/x-gps");
        contentTypeMap.put("gtar", "application/x-gtar");
        contentTypeMap.put("gz", "application/x-gzip");
        contentTypeMap.put("hdf", "application/x-hdf");
        contentTypeMap.put("hdm", "text/x-hdml");
        contentTypeMap.put("hdml", "text/x-hdml");
        contentTypeMap.put("hlp", "application/winhlp");
        contentTypeMap.put("hqx", "application/mac-binhex40");
        contentTypeMap.put("htm", "text/html");
        contentTypeMap.put("html", "text/html");
        contentTypeMap.put("hts", "text/html");
        contentTypeMap.put("ice", "x-conference/x-cooltalk");
        contentTypeMap.put("ico", "application/octet-stream");
        contentTypeMap.put("ief", "image/ief");
        contentTypeMap.put("ifm", "image/gif");
        contentTypeMap.put("ifs", "image/ifs");
        contentTypeMap.put("imy", "audio/melody");
        contentTypeMap.put("ins", "application/x-NET-Install");
        contentTypeMap.put("ips", "application/x-ipscript");
        contentTypeMap.put("ipx", "application/x-ipix");
        contentTypeMap.put("it", "audio/x-mod");
        contentTypeMap.put("itz", "audio/x-mod");
        contentTypeMap.put("ivr", "i-world/i-vrml");
        contentTypeMap.put("j2k", "image/j2k");
        contentTypeMap.put("jad", "text/vnd.sun.j2me.app-descriptor");
        contentTypeMap.put("jam", "application/x-jam");
        contentTypeMap.put("jar", "application/java-archive");
        contentTypeMap.put("jnlp", "application/x-java-jnlp-file");
        contentTypeMap.put("jpe", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpz", "image/jpeg");
        contentTypeMap.put("js", "application/x-javascript");
        contentTypeMap.put("jwc", "application/jwc");
        contentTypeMap.put("kjx", "application/x-kjx");
        contentTypeMap.put("lak", "x-lml/x-lak");
        contentTypeMap.put("latex", "application/x-latex");
        contentTypeMap.put("lcc", "application/fastman");
        contentTypeMap.put("lcl", "application/x-digitalloca");
        contentTypeMap.put("lcr", "application/x-digitalloca");
        contentTypeMap.put("lgh", "application/lgh");
        contentTypeMap.put("lha", "application/octet-stream");
        contentTypeMap.put("lml", "x-lml/x-lml");
        contentTypeMap.put("lmlpack", "x-lml/x-lmlpack");
        contentTypeMap.put("lsf", "video/x-ms-asf");
        contentTypeMap.put("lsx", "video/x-ms-asf");
        contentTypeMap.put("lzh", "application/x-lzh");
        contentTypeMap.put("m13", "application/x-msmediaview");
        contentTypeMap.put("m14", "application/x-msmediaview");
        contentTypeMap.put("m15", "audio/x-mod");
        contentTypeMap.put("m3u", "audio/x-mpegurl");
        contentTypeMap.put("m3url", "audio/x-mpegurl");
        contentTypeMap.put("ma1", "audio/ma1");
        contentTypeMap.put("ma2", "audio/ma2");
        contentTypeMap.put("ma3", "audio/ma3");
        contentTypeMap.put("ma5", "audio/ma5");
        contentTypeMap.put("man", "application/x-troff-man");
        contentTypeMap.put("map", "magnus-internal/imagemap");
        contentTypeMap.put("mbd", "application/mbedlet");
        contentTypeMap.put("mct", "application/x-mascot");
        contentTypeMap.put("mdb", "application/x-msaccess");
        contentTypeMap.put("mdz", "audio/x-mod");
        contentTypeMap.put("me", "application/x-troff-me");
        contentTypeMap.put("mel", "text/x-vmel");
        contentTypeMap.put("mi", "application/x-mif");
        contentTypeMap.put("mid", "audio/midi");
        contentTypeMap.put("midi", "audio/midi");
        contentTypeMap.put("mif", "application/x-mif");
        contentTypeMap.put("mil", "image/x-cals");
        contentTypeMap.put("mio", "audio/x-mio");
        contentTypeMap.put("mmf", "application/x-skt-lbs");
        contentTypeMap.put("mng", "video/x-mng");
        contentTypeMap.put("mny", "application/x-msmoney");
        contentTypeMap.put("moc", "application/x-mocha");
        contentTypeMap.put("mocha", "application/x-mocha");
        contentTypeMap.put("mod", "audio/x-mod");
        contentTypeMap.put("mof", "application/x-yumekara");
        contentTypeMap.put("mol", "chemical/x-mdl-molfile");
        contentTypeMap.put("mop", "chemical/x-mopac-input");
        contentTypeMap.put("mov", "video/quicktime");
        contentTypeMap.put("movie", "video/x-sgi-movie");
        contentTypeMap.put("mp2", "audio/x-mpeg");
        contentTypeMap.put("mp3", "audio/x-mpeg");
        contentTypeMap.put("mp4", "video/mp4");
        contentTypeMap.put("mpc", "application/vnd.mpohun.certificate");
        contentTypeMap.put("mpe", "video/mpeg");
        contentTypeMap.put("mpeg", "video/mpeg");
        contentTypeMap.put("mpg", "video/mpeg");
        contentTypeMap.put("mpg4", "video/mp4");
        contentTypeMap.put("mpga", "audio/mpeg");
        contentTypeMap.put("mpn", "application/vnd.mophun.application");
        contentTypeMap.put("mpp", "application/vnd.ms-project");
        contentTypeMap.put("mps", "application/x-mapserver");
        contentTypeMap.put("mrl", "text/x-mrml");
        contentTypeMap.put("mrm", "application/x-mrm");
        contentTypeMap.put("ms", "application/x-troff-ms");
        contentTypeMap.put("mts", "application/metastream");
        contentTypeMap.put("mtx", "application/metastream");
        contentTypeMap.put("mtz", "application/metastream");
        contentTypeMap.put("mzv", "application/metastream");
        contentTypeMap.put("nar", "application/zip");
        contentTypeMap.put("nbmp", "image/nbmp");
        contentTypeMap.put("nc", "application/x-netcdf");
        contentTypeMap.put("ndb", "x-lml/x-ndb");
        contentTypeMap.put("ndwn", "application/ndwn");
        contentTypeMap.put("nif", "application/x-nif");
        contentTypeMap.put("nmz", "application/x-scream");
        contentTypeMap.put("nokia-op-logo", "image/vnd.nok-oplogo-color");
        contentTypeMap.put("npx", "application/x-netfpx");
        contentTypeMap.put("nsnd", "audio/nsnd");
        contentTypeMap.put("nva", "application/x-neva1");
        contentTypeMap.put("oda", "application/oda");
        contentTypeMap.put("oom", "application/x-AtlasMate-Plugin");
        contentTypeMap.put("pac", "audio/x-pac");
        contentTypeMap.put("pae", "audio/x-epac");
        contentTypeMap.put("pan", "application/x-pan");
        contentTypeMap.put("pbm", "image/x-portable-bitmap");
        contentTypeMap.put("pcx", "image/x-pcx");
        contentTypeMap.put("pda", "image/x-pda");
        contentTypeMap.put("pdb", "chemical/x-pdb");
        contentTypeMap.put("pdf", "application/pdf");
        contentTypeMap.put("pfr", "application/font-tdpfr");
        contentTypeMap.put("pgm", "image/x-portable-graymap");
        contentTypeMap.put("pict", "image/x-pict");
        contentTypeMap.put("pm", "application/x-perl");
        contentTypeMap.put("pmd", "application/x-pmd");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("pnm", "image/x-portable-anymap");
        contentTypeMap.put("pnz", "image/png");
        contentTypeMap.put("pot", "application/vnd.ms-powerpoint");
        contentTypeMap.put("ppm", "image/x-portable-pixmap");
        contentTypeMap.put("pps", "application/vnd.ms-powerpoint");
        contentTypeMap.put("ppt", "application/vnd.ms-powerpoint");
        contentTypeMap.put("pqf", "application/x-cprplayer");
        contentTypeMap.put("pqi", "application/cprplayer");
        contentTypeMap.put("prc", "application/x-prc");
        contentTypeMap.put("proxy", "application/x-ns-proxy-autoconfig");
        contentTypeMap.put("ps", "application/postscript");
        contentTypeMap.put("ptlk", "application/listenup");
        contentTypeMap.put("pub", "application/x-mspublisher");
        contentTypeMap.put("pvx", "video/x-pv-pvx");
        contentTypeMap.put("qcp", "audio/vnd.qcelp");
        contentTypeMap.put("qt", "video/quicktime");
        contentTypeMap.put("qti", "image/x-quicktime");
        contentTypeMap.put("qtif", "image/x-quicktime");
        contentTypeMap.put("r3t", "text/vnd.rn-realtext3d");
        contentTypeMap.put("ra", "audio/x-pn-realaudio");
        contentTypeMap.put("ram", "audio/x-pn-realaudio");
        contentTypeMap.put("rar", "application/x-rar-compressed");
        contentTypeMap.put("ras", "image/x-cmu-raster");
        contentTypeMap.put("rdf", "application/rdf+xml");
        contentTypeMap.put("rf", "image/vnd.rn-realflash");
        contentTypeMap.put("rgb", "image/x-rgb");
        contentTypeMap.put("rlf", "application/x-richlink");
        contentTypeMap.put("rm", "audio/x-pn-realaudio");
        contentTypeMap.put("rmf", "audio/x-rmf");
        contentTypeMap.put("rmm", "audio/x-pn-realaudio");
        contentTypeMap.put("rmvb", "audio/x-pn-realaudio");
        contentTypeMap.put("rnx", "application/vnd.rn-realplayer");
        contentTypeMap.put("roff", "application/x-troff");
        contentTypeMap.put("rp", "image/vnd.rn-realpix");
        contentTypeMap.put("rpm", "audio/x-pn-realaudio-plugin");
        contentTypeMap.put("rt", "text/vnd.rn-realtext");
        contentTypeMap.put("rte", "x-lml/x-gps");
        contentTypeMap.put("rtf", "application/rtf");
        contentTypeMap.put("rtg", "application/metastream");
        contentTypeMap.put("rtx", "text/richtext");
        contentTypeMap.put("rv", "video/vnd.rn-realvideo");
        contentTypeMap.put("rwc", "application/x-rogerwilco");
        contentTypeMap.put("s3m", "audio/x-mod");
        contentTypeMap.put("s3z", "audio/x-mod");
        contentTypeMap.put("sca", "application/x-supercard");
        contentTypeMap.put("scd", "application/x-msschedule");
        contentTypeMap.put("sdf", "application/e-score");
        contentTypeMap.put("sea", "application/x-stuffit");
        contentTypeMap.put("sgm", "text/x-sgml");
        contentTypeMap.put("sgml", "text/x-sgml");
        contentTypeMap.put("sh", "application/x-sh");
        contentTypeMap.put("shar", "application/x-shar");
        contentTypeMap.put("shtml", "magnus-internal/parsed-html");
        contentTypeMap.put("shw", "application/presentations");
        contentTypeMap.put("si6", "image/si6");
        contentTypeMap.put("si7", "image/vnd.stiwap.sis");
        contentTypeMap.put("si9", "image/vnd.lgtwap.sis");
        contentTypeMap.put("sis", "application/vnd.symbian.install");
        contentTypeMap.put("sit", "application/x-stuffit");
        contentTypeMap.put("skd", "application/x-Koan");
        contentTypeMap.put("skm", "application/x-Koan");
        contentTypeMap.put("skp", "application/x-Koan");
        contentTypeMap.put("skt", "application/x-Koan");
        contentTypeMap.put("slc", "application/x-salsa");
        contentTypeMap.put("smd", "audio/x-smd");
        contentTypeMap.put("smi", "application/smil");
        contentTypeMap.put("smil", "application/smil");
        contentTypeMap.put("smp", "application/studiom");
        contentTypeMap.put("smz", "audio/x-smd");
        contentTypeMap.put("snd", "audio/basic");
        contentTypeMap.put("spc", "text/x-speech");
        contentTypeMap.put("spl", "application/futuresplash");
        contentTypeMap.put("spr", "application/x-sprite");
        contentTypeMap.put("sprite", "application/x-sprite");
        contentTypeMap.put("spt", "application/x-spt");
        contentTypeMap.put("src", "application/x-wais-source");
        contentTypeMap.put("stk", "application/hyperstudio");
        contentTypeMap.put("stm", "audio/x-mod");
        contentTypeMap.put("sv4cpio", "application/x-sv4cpio");
        contentTypeMap.put("sv4crc", "application/x-sv4crc");
        contentTypeMap.put("svf", "image/vnd");
        contentTypeMap.put("svg", "image/svg-xml");
        contentTypeMap.put("svh", "image/svh");
        contentTypeMap.put("svr", "x-world/x-svr");
        contentTypeMap.put("swf", "application/x-shockwave-flash");
        contentTypeMap.put("swfl", "application/x-shockwave-flash");
        contentTypeMap.put("t", "application/x-troff");
        contentTypeMap.put("tad", "application/octet-stream");
        contentTypeMap.put("talk", "text/x-speech");
        contentTypeMap.put("tar", "application/x-tar");
        contentTypeMap.put("taz", "application/x-tar");
        contentTypeMap.put("tbp", "application/x-timbuktu");
        contentTypeMap.put("tbt", "application/x-timbuktu");
        contentTypeMap.put("tcl", "application/x-tcl");
        contentTypeMap.put("tex", "application/x-tex");
        contentTypeMap.put("texi", "application/x-texinfo");
        contentTypeMap.put("texinfo", "application/x-texinfo");
        contentTypeMap.put("tgz", "application/x-tar");
        contentTypeMap.put("thm", "application/vnd.eri.thm");
        contentTypeMap.put("tif", "image/tiff");
        contentTypeMap.put("tiff", "image/tiff");
        contentTypeMap.put("tki", "application/x-tkined");
        contentTypeMap.put("tkined", "application/x-tkined");
        contentTypeMap.put("toc", "application/toc");
        contentTypeMap.put("toy", "image/toy");
        contentTypeMap.put("tr", "application/x-troff");
        contentTypeMap.put("trk", "x-lml/x-gps");
        contentTypeMap.put("trm", "application/x-msterminal");
        contentTypeMap.put("tsi", "audio/tsplayer");
        contentTypeMap.put("tsp", "application/dsptype");
        contentTypeMap.put("tsv", "text/tab-separated-values");
        contentTypeMap.put("tsv", "text/tab-separated-values");
        contentTypeMap.put("ttf", "application/octet-stream");
        contentTypeMap.put("ttz", "application/t-time");
        contentTypeMap.put("txt", "text/plain");
        contentTypeMap.put("ult", "audio/x-mod");
        contentTypeMap.put("ustar", "application/x-ustar");
        contentTypeMap.put("uu", "application/x-uuencode");
        contentTypeMap.put("uue", "application/x-uuencode");
        contentTypeMap.put("vcd", "application/x-cdlink");
        contentTypeMap.put("vcf", "text/x-vcard");
        contentTypeMap.put("vdo", "video/vdo");
        contentTypeMap.put("vib", "audio/vib");
        contentTypeMap.put("viv", "video/vivo");
        contentTypeMap.put("vivo", "video/vivo");
        contentTypeMap.put("vmd", "application/vocaltec-media-desc");
        contentTypeMap.put("vmf", "application/vocaltec-media-file");
        contentTypeMap.put("vmi", "application/x-dreamcast-vms-info");
        contentTypeMap.put("vms", "application/x-dreamcast-vms");
        contentTypeMap.put("vox", "audio/voxware");
        contentTypeMap.put("vqe", "audio/x-twinvq-plugin");
        contentTypeMap.put("vqf", "audio/x-twinvq");
        contentTypeMap.put("vql", "audio/x-twinvq");
        contentTypeMap.put("vre", "x-world/x-vream");
        contentTypeMap.put("vrml", "x-world/x-vrml");
        contentTypeMap.put("vrt", "x-world/x-vrt");
        contentTypeMap.put("vrw", "x-world/x-vream");
        contentTypeMap.put("vts", "workbook/formulaone");
        contentTypeMap.put("wav", "audio/x-wav");
        contentTypeMap.put("wax", "audio/x-ms-wax");
        contentTypeMap.put("wbmp", "image/vnd.wap.wbmp");
        contentTypeMap.put("web", "application/vnd.xara");
        contentTypeMap.put("wi", "image/wavelet");
        contentTypeMap.put("wis", "application/x-InstallShield");
        contentTypeMap.put("wm", "video/x-ms-wm");
        contentTypeMap.put("wma", "audio/x-ms-wma");
        contentTypeMap.put("wmd", "application/x-ms-wmd");
        contentTypeMap.put("wmf", "application/x-msmetafile");
        contentTypeMap.put("wml", "text/vnd.wap.wml");
        contentTypeMap.put("wmlc", "application/vnd.wap.wmlc");
        contentTypeMap.put("wmls", "text/vnd.wap.wmlscript");
        contentTypeMap.put("wmlsc", "application/vnd.wap.wmlscriptc");
        contentTypeMap.put("wmlscript", "text/vnd.wap.wmlscript");
        contentTypeMap.put("wmv", "audio/x-ms-wmv");
        contentTypeMap.put("wmx", "video/x-ms-wmx");
        contentTypeMap.put("wmz", "application/x-ms-wmz");
        contentTypeMap.put("wpng", "image/x-up-wpng");
        contentTypeMap.put("wpt", "x-lml/x-gps");
        contentTypeMap.put("wri", "application/x-mswrite");
        contentTypeMap.put("wrl", "x-world/x-vrml");
        contentTypeMap.put("wrz", "x-world/x-vrml");
        contentTypeMap.put("ws", "text/vnd.wap.wmlscript");
        contentTypeMap.put("wsc", "application/vnd.wap.wmlscriptc");
        contentTypeMap.put("wv", "video/wavelet");
        contentTypeMap.put("wvx", "video/x-ms-wvx");
        contentTypeMap.put("wxl", "application/x-wxl");
        contentTypeMap.put("x-gzip", "application/x-gzip");
        contentTypeMap.put("xar", "application/vnd.xara");
        contentTypeMap.put("xbm", "image/x-xbitmap");
        contentTypeMap.put("xdm", "application/x-xdma");
        contentTypeMap.put("xdma", "application/x-xdma");
        contentTypeMap.put("xdw", "application/vnd.fujixerox.docuworks");
        contentTypeMap.put("xht", "application/xhtml+xml");
        contentTypeMap.put("xhtm", "application/xhtml+xml");
        contentTypeMap.put("xhtml", "application/xhtml+xml");
        contentTypeMap.put("xla", "application/vnd.ms-excel");
        contentTypeMap.put("xlc", "application/vnd.ms-excel");
        contentTypeMap.put("xll", "application/x-excel");
        contentTypeMap.put("xlm", "application/vnd.ms-excel");
        contentTypeMap.put("xls", "application/vnd.ms-excel");
        contentTypeMap.put("xlt", "application/vnd.ms-excel");
        contentTypeMap.put("xlw", "application/vnd.ms-excel");
        contentTypeMap.put("xm", "audio/x-mod");
        contentTypeMap.put("xml", "text/xml");
        contentTypeMap.put("xmz", "audio/x-mod");
        contentTypeMap.put("xpi", "application/x-xpinstall");
        contentTypeMap.put("xpm", "image/x-xpixmap");
        contentTypeMap.put("xsit", "text/xml");
        contentTypeMap.put("xsl", "text/xml");
        contentTypeMap.put("xul", "text/xul");
        contentTypeMap.put("xwd", "image/x-xwindowdump");
        contentTypeMap.put("xyz", "chemical/x-pdb");
        contentTypeMap.put("yz1", "application/x-yz1");
        contentTypeMap.put("z", "application/x-compress");
        contentTypeMap.put("zac", "application/x-zaurus-zac");
        contentTypeMap.put("zip", "application/zip");
    }
}
