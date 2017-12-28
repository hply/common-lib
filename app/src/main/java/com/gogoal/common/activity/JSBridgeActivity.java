package com.gogoal.common.activity;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.gogoal.common.R;
import com.gogoal.common.base.BaseActivity;
import com.gogoal.common.common.WebViewUtil;

import cn.iyuxuan.library.jsbridge.BridgeHandler;
import cn.iyuxuan.library.jsbridge.BridgeWebView;
import cn.iyuxuan.library.jsbridge.DefaultHandler;

/**
 * @author wangjd on 2017/12/4 0004.
 * @description :${annotated}.
 */

public class JSBridgeActivity extends BaseActivity {

    private BridgeWebView bWebView;

    @Override
    public int bindLayout() {
        return R.layout.activity_jsbridge;
    }

    @Override
    public void initView(View mContentView) {
        bWebView = findViewById(R.id.bridge_web);
    }

    @Override
    public void doBusiness(final Context mContext) {
        initWebView(bWebView);

        bWebView.setOnWebChangeListener(new BridgeWebView.WebChangeListener() {
            @Override
            public void onWebLoadFinish(String url, String title) {
                setTitle(title);
            }
        });

        String webUrl = getIntent().getStringExtra("web_url");
        if (TextUtils.isEmpty(webUrl)) {
            bWebView.loadUrl(webUrl);
        } else {
            bWebView.loadUrl(webUrl);
        }
        setTitle(bWebView.getTitle());

        bWebView.registerHandler("applyPay", new BridgeHandler() {
            @Override
            public void handler(String data, ValueCallback<String> function) {
                Log.e("TAG", data);
                Toast.makeText(mContext, "daata="+data, Toast.LENGTH_SHORT).show();
            }
        });

        //1.隐藏分享按钮的方法
        bWebView.registerHandler("shareSupport", new BridgeHandler() {
            @Override
            public void handler(String data, final ValueCallback<String> function) {
                Log.e("TAG", data);
            }
        });

    }

    private void initWebView(BridgeWebView mWebView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView.addJavascriptInterface(new WebViewUtil(this), "interaction");

        WebSettings settings = mWebView.getSettings();
        mWebView.setDefaultHandler(new DefaultHandler());

        settings.setBuiltInZoomControls(false);

        String ua = mWebView.getSettings().getUserAgentString() + "GoGoaler-invest/v1.1.4";
        //Mozilla/5.0 (Linux; Android 7.1.1; MI MAX 2 Build/NMF26F; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/61.0.3163.98 Mobile Safari/537.36GoGoaler-invest/v1.1.4

        mWebView.getSettings().setUserAgentString(ua);

        // 开启DOM缓存。
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setDomStorageEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        settings.setAppCachePath(appCachePath);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
    }

}
