package cn.iyuxuan.library.jsbridge;


import android.webkit.ValueCallback;

interface WebViewJavascriptBridge {
    void send(String data);
    void send(String data, ValueCallback<String> responseCallback);
}
