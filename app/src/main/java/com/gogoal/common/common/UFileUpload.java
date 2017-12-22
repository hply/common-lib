package com.gogoal.common.common;

import android.util.Base64;

import java.io.File;

import cn.iyuxuan.library.uFileLib.UFileCallBack;
import cn.iyuxuan.library.uFileLib.UFileRequest;
import cn.iyuxuan.library.uFileLib.UFileSDK;
import cn.iyuxuan.library.uFileLib.UFileUtils;

/**
 * /**
 * @author wangjd on 2017/2/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * <p>
 * ufile上传的封装——半成品
 */

public class UFileUpload {

    private static final String BUCKET = "hackfile";

    private UFileUpload(UFileSDK uFileSDK) {
        this.uFileSDK = uFileSDK;
    }

    public static UFileUpload getInstance() {
        return new UFileUpload(new UFileSDK(BUCKET));
    }

    private UFileSDK uFileSDK;

    public interface UploadListener {

        /**
         * 上传过程中...
         *
         * @param progress 上传进度
         */
        void onUploading(int progress);

        /**
         * 上传成功
         *
         * @param onlineUri 上传完成后的url
         */
        void onSuccess(String onlineUri);

        /**
         * 上传失败
         */
        void onFailed();
    }

    /**
     * 枚举上传四中类型
     */
    public enum Type {
        /**
         * 图片
         */
        IMAGE(0),

        /**
         * 音频
         */
        AUDIO(1),

        /**
         * 视频
         */
        VIDEO(2),

        /**
         * 其他文件
         */
        FILE(4);

        int type;

        Type(int type) {
            this.type = type;
        }

    }

    public void upload(final File file, Type type, final UploadListener listener) {
        String contentType = null;
        switch (type) {
            case IMAGE:
                contentType = ImageUtils.getImageType(file);
                break;
            case AUDIO:
                contentType = "audio/amr";
                break;
            case VIDEO:
                contentType = "video/mpeg4";
                break;
            case FILE:
                contentType = "application/octet-stream";
                break;
            default:
                break;
        }

        upload(file, contentType, listener);

    }
    public void upload(final File file, String contentType, final UploadListener listener) {

        String httpMethod = "PUT";

        String contentMD5 = UFileUtils.getFileMD5(file);

        String date = "";

        String keyName = "gogoal" + File.separator + "avatar" + File.separator + "ucloud_" +
                MD5Utils.getMD5EncryptyString16(file.getPath()) +
                file.getPath().substring(file.getPath().lastIndexOf('.'));

        String authorization = getAuthorization(httpMethod, contentMD5, contentType, date, BUCKET, keyName);
        final UFileRequest request = new UFileRequest();
        request.setHttpMethod(httpMethod);
        request.setAuthorization(authorization);
        request.setContentMD5(contentMD5);
        request.setContentType(contentType);

//        http://hackfile.ufile.ucloud.com.cn
        uFileSDK.putFile(request, file, keyName, new UFileCallBack() {
            @Override
            public void onSuccess(org.json.JSONObject response) {
                listener.onSuccess(uFileSDK.getUrl());
            }

            @Override
            public void onProcess(long len) {
                listener.onUploading((int) (len * 100 / file.length()));
            }

            @Override
            public void onFail(org.json.JSONObject response) {
                listener.onFailed();
            }
        });

    }

//    private String decode(String separator) {
//        try {
//            return URLDecoder.decode(separator, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "/";
//    }

    /**
     * UCloud签名
     */
    private String getAuthorization(String httpMethod, String contentMD5, String contentType, String date, String bucket, String key) {
        String signature = "";
        try {
            String strToSign = httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + "/" + bucket + "/" + key;
            byte[] hmac = UFileUtils.hmacSha1(AppConst.PRIVATE_KEY, strToSign);
            signature = Base64.encodeToString(hmac, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UCloud" + " " + AppConst.PUBLIC_KEY + ":" + signature;
    }
}
