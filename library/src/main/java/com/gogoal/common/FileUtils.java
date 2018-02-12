package com.gogoal.common;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author wangjd on 2017/12/27 10:46.
 * @description :${annotated}.
 */
public class FileUtils {
    /**
     * 关闭流
     */
    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(
                        FileUtils.class.getClass().getName(), e);
            }
        }
    }

    //删除文件夹
    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        // 目录不存在返回true
        if (!dir.exists()) {
            return true;
        }
        // 不是目录返回false
        if (!dir.isDirectory()) {
            return false;
        }
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

    //删除文件
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * 获取文件大小
     *
     * @param file 文件
     * @return 文件大小
     */
    public static String getFileSize(File file) {
        if ((file == null) || (!file.exists())) {
            return "";
        }
        return byte2FitSize(file.length());
    }


    /**
     * 字节数转合适大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 1...1024 unit
     */
    private static String byte2FitSize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.getDefault(), "%.3fB", (double) byteNum);
        } else if (byteNum < 1048576) {
            return String.format(Locale.getDefault(), "%.3fKB", (double) byteNum / 1024);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.getDefault(), "%.3fMB", (double) byteNum / 1048576);
        } else {
            return String.format(Locale.getDefault(), "%.3fGB", (double) byteNum / 1073741824);
        }
    }

    //获取文件夹大小
    public static String getDirSize(File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    private static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024) {
            return String.format(Locale.CHINA, "%.3fB", byteNum + 0.0005);
        } else if (byteNum < 1048576) {
            return String.format(Locale.CHINA, "%.3fKB", byteNum / 1024 + 0.0005);
        } else if (byteNum < 1073741824) {
            return String.format(Locale.CHINA, "%.3fMB", byteNum / 1048576 + 0.0005);
        } else {
            return String.format(Locale.CHINA, "%.3fGB", byteNum / 1073741824 + 0.0005);
        }
    }


    /**
     * 获取目录长度
     *
     * @param dir 目录
     * @return 文件大小
     */
    private static long getDirLength(File dir) {
        if (!(dir != null && dir.exists() && dir.isDirectory())) {
            return -1;
        }
        long len = 0;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    len += getDirLength(file);
                } else {
                    len += file.length();
                }
            }
        }
        return len;
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(File file) {
        String substring = file.getPath().substring(file.getPath().lastIndexOf('.') + 1);
        System.err.println(substring);
        if (substring.contains("/")) {
            return substring.substring(0, substring.indexOf("/"));
        } else if (substring.contains("?")) {
            return substring.substring(0, substring.indexOf("?"));
        } else {
            return substring;
        }
    }

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
                return cursor.getString(cursor.getColumnIndexOrThrow(column));
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 文件复制.
     */
    public static boolean copy(String srcFile, String destFile) {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    //==================================================================
    // 缓存文件头信息-文件头信息
    private static final HashMap<String, String> MAP_FILE_TYPES = new HashMap<String, String>();

    static {
        // images
        MAP_FILE_TYPES.put("FFD8FF", "jpg");
        MAP_FILE_TYPES.put("52494646", "webp");
        MAP_FILE_TYPES.put("89504E47", "png");
        MAP_FILE_TYPES.put("47494638", "gif");
        MAP_FILE_TYPES.put("49492A00", "tif");
        MAP_FILE_TYPES.put("424D", "bmp");
        //
        MAP_FILE_TYPES.put("41433130", "dwg"); // CAD
        MAP_FILE_TYPES.put("38425053", "psd");
        MAP_FILE_TYPES.put("7B5C727466", "rtf"); // 日记本
        MAP_FILE_TYPES.put("3C3F786D6C", "xml");
        MAP_FILE_TYPES.put("68746D6C3E", "html");
        MAP_FILE_TYPES.put("44656C69766572792D646174653A", "eml"); // 邮件
        MAP_FILE_TYPES.put("D0CF11E0", "doc");
        MAP_FILE_TYPES.put("5374616E64617264204A", "mdb");
        MAP_FILE_TYPES.put("252150532D41646F6265", "ps");
        MAP_FILE_TYPES.put("255044462D312E", "pdf");
        MAP_FILE_TYPES.put("504B0304", "docx");
        MAP_FILE_TYPES.put("52617221", "rar");
        MAP_FILE_TYPES.put("57415645", "wav");
        MAP_FILE_TYPES.put("41564920", "avi");
        MAP_FILE_TYPES.put("2E524D46", "rm");
        MAP_FILE_TYPES.put("000001BA", "mpg");
        MAP_FILE_TYPES.put("000001B3", "mpg");
        MAP_FILE_TYPES.put("6D6F6F76", "mov");
        MAP_FILE_TYPES.put("3026B2758E66CF11", "asf");
        MAP_FILE_TYPES.put("4D546864", "mid");
        MAP_FILE_TYPES.put("1F8B08", "gz");
    }

    /**
     * 根据文件路径获取文件头信息
     *
     * @param filePath 文件路径
     * @return 文件头信息
     */
    public static String getFileType(File filePath) {
        String header = getFileHeader(filePath);
        String type = MAP_FILE_TYPES.get(header);
        if (type != null) {
            return type;
        } else {
            for (Map.Entry<String, String> entry : MAP_FILE_TYPES.entrySet()) {
                if (entry.getKey().contains(header) || header.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return "";

        }
    }

    /**
     * 根据文对象获取文件头信息
     *
     * @param filePath 文件路径
     * @return 文件头信息
     */
    public static String getFileHeader(File filePath) {
        InputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.getMessage();
        } finally {
            closeIO(is);
        }
        return value;
    }

    /**
     * 将要读取文件头信息的文件的byte数组转换成string类型表示
     *
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息 下面这段代码就是用来对文件类型作验证的方法， 第一个参数是文件的字节数组，第二个就是定义的可通过类型。代码很简单，
     * 主要是注意中间的一处，将字节数组的前四位转换成16进制字符串，并且转换的时候，要先和0xFF做一次与运算。这是因为，
     * 整个文件流的字节数组中，有很多是负数，进行了与运算后，可以将前面的符号位都去掉，这样转换成的16进制字符串最多保留两位，
     * 如果是正数又小于10，那么转换后只有一位，需要在前面补0，这样做的目的是方便比较，取完前四位这个循环就可以终止了。
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
