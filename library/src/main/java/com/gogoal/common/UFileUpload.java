package com.gogoal.common;

import android.util.Base64;

import java.io.File;

import cn.iyuxuan.library.uFileLib.UFileCallBack;
import cn.iyuxuan.library.uFileLib.UFileRequest;
import cn.iyuxuan.library.uFileLib.UFileSDK;
import cn.iyuxuan.library.uFileLib.UFileUtils;

/**
 * /**
 *
 * @author wangjd on 2017/2/13 0013.
 *         Staff_id 1375
 *         phone 18930640263
 *         <p>
 *         ufile上传的封装——半成品
 */

public class UFileUpload {

    private static String PRIVATE_KEY;
    private static String PUBLIC_KEY;

    public static void init(String privateKey, String publicKey) {
        PRIVATE_KEY = privateKey;
        PUBLIC_KEY = publicKey;
    }

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

    public void upload(final File file, String contentType, final UploadListener listener) {
        String fileName = file.getName();
        upload(file, contentType, fileName, listener);
    }

    public void upload(final File file, String contentType, String name, final UploadListener listener) {

        String httpMethod = "PUT";

        String contentMD5 = UFileUtils.getFileMD5(file);

        String date = "";

        //上传文件的最终保存的名字
        String keyName = "gogoal" + File.separator +
                FileUtils.getFileExtension(file) + File.separator +
                name;

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
                if (listener != null) {
                    listener.onSuccess(uFileSDK.getUrl());
                }
            }

            @Override
            public void onProcess(long len) {
                if (listener != null) {
                    listener.onUploading((int) (len * 100 / file.length()));
                }
            }

            @Override
            public void onFail(org.json.JSONObject response) {
                if (listener != null) {
                    listener.onFailed();
                }
            }
        });

    }

    /**
     * UCloud签名
     */
    private String getAuthorization(String httpMethod, String contentMD5, String contentType, String date, String bucket, String key) {
        if (PRIVATE_KEY == null || PUBLIC_KEY == null) {
            throw new IllegalArgumentException("请在项目的Application中初始化UFileUpload，" +
                    "调用UFileUpload.init(pri,pub)配置私钥和公钥");
        }
        String signature = "";
        try {
            String strToSign = httpMethod + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + "/" + bucket + "/" + key;
            byte[] hmac = UFileUtils.hmacSha1(PRIVATE_KEY, strToSign);
            signature = Base64.encodeToString(hmac, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UCloud" + " " + PUBLIC_KEY + ":" + signature;
    }
}
