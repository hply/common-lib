package cn.iyuxuan.library.uFileLib;

import org.json.JSONObject;

/**
 * 回调函数  所有回调均在主线程
 */
public interface UFileCallBack {
    void onSuccess(JSONObject message);

    void onProcess(long len);

    void onFail(JSONObject message);
}