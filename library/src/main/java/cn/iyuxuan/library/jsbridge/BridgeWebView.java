package cn.iyuxuan.library.jsbridge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.ProgressBar;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.iyuxuan.library.R;


public class BridgeWebView extends WebView implements WebViewJavascriptBridge {

    private ProgressBar progressbar;
    private Context context;

    public interface WebChangeListener {
        void onWebLoadFinish(String url, String title);
    }

    public WebChangeListener mChangeListener;

    public WebChangeListener getChangeListener() {
        return mChangeListener;
    }

    public void setOnWebChangeListener(WebChangeListener changeListener) {
        if (changeListener != null) {
            mChangeListener = changeListener;
        }
    }

    Map<String, ValueCallback<String>> responseCallbacks = new HashMap<>();
    BridgeHandler defaultHandler = new DefaultHandler();

    /**
     * @param handler default handler,handle messages send by js without assigned handler name,
     *                if js message has handler name, it will be handled by named handlers registered by native
     */
    public void setDefaultHandler(BridgeHandler handler) {
        this.defaultHandler = handler;
    }

    Map<String, BridgeHandler> messageHandlers = new HashMap<>();

    public Map<String, BridgeHandler> getMessageHandlers() {
        return messageHandlers;
    }

    private List<Message> startupMessage = new ArrayList<>();

    public List<Message> getStartupMessage() {
        return startupMessage;
    }

    public void setStartupMessage(List<Message> startupMessage) {
        this.startupMessage = startupMessage;
    }

    private long uniqueId = 0;

    public interface SystemIntentInteface {
        void setSystemData(String data);
    }

    private SystemIntentInteface mSystemIntentInteface;

    public SystemIntentInteface getSystemIntentInteface() {
        return mSystemIntentInteface;
    }

    public void setSystemIntentInteface(SystemIntentInteface systemIntentInteface) {
        mSystemIntentInteface = systemIntentInteface;
    }

    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BridgeWebView(Context context) {
        super(context);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, Utils.dp2px(context, 3), 0, 0));
        progressbar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.web_progress));
        addView(progressbar);

        setWebChromeClient(new WebChromeClient());

        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.getSettings().setJavaScriptEnabled(true);
        this.setWebViewClient(generateBridgeWebViewClient());
    }

    public PageLoadFinishListener getFinishListener() {
        return new PageLoadFinishListener() {
            @Override
            public void onFinish(String url, String title) {
                getChangeListener().onWebLoadFinish(url, title);
            }
        };
    }

    protected BridgeWebViewClient generateBridgeWebViewClient() {
        return new BridgeWebViewClient(this, getFinishListener());
    }

    void handlerReturnData(String url) {
        String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
        ValueCallback<String> f = responseCallbacks.get(functionName);
        String data = BridgeUtil.getDataFromReturnUrl(url);
        if (f != null) {
            f.onReceiveValue(data);
            responseCallbacks.remove(functionName);
        }
    }

    @Override
    public void send(String data) {
        send(data, null);
    }

    @Override
    public void send(String data, ValueCallback<String> responseCallback) {
        doSend(null, data, responseCallback);
    }

    private void doSend(String handlerName, String data, ValueCallback<String> responseCallback) {
        Message m = new Message();
        if (BridgeUtil.isPresent(data)) {
            m.setData(data);
        }
        if (BridgeUtil.isPresent(handlerName)) {
            m.setHandlerName(handlerName);
        }
        if (responseCallback != null) {
            String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
            responseCallbacks.put(callbackStr, responseCallback);
            m.setCallbackId(callbackStr);
        }
        queueMessage(m);
    }

    private void queueMessage(Message m) {
        if (startupMessage != null) {
            startupMessage.add(m);
        } else {
            dispatchMessage(m);
        }
    }

    void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        String javascriptCommand;
        messageJson = BridgeUtil.escapeJsonString(messageJson);
        javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        ValueCallback<String> callback = responseCallbacks.get(m.getCallbackId());
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            this.loadUrl(javascriptCommand, callback);
        }
    }

    void flushMessageQueue() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String data) {
                    if (BridgeUtil.useEvaluateJS()) {
                        handleMessageGE19(data);
                    } else {
                        handleMessageLT19(data);
                    }
                }
            });
        }
    }

    private void handleMessageGE19(String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        // remove duplicate quotation
        if (url.startsWith("\"") && url.endsWith("\"")) {
            url = url.substring(1, url.length() - 1);
        }
        if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
            String data = BridgeUtil.getDataFromReturnUrl(url);
            handleMessageList(data);
        } else {
//            Log.e(String.format("Error Happened %s", url));
        }
    }

    private void handleMessageLT19(String data) {
        handleMessageList(data);
    }

    private void handleMessageList(String data) {
        // deserializeMessage
        List<Message> msgList;
        try {
            msgList = Message.toArrayList(data);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (BridgeUtil.isBlank(msgList)) {
            return;
        }

        for (Message msg : msgList) {
            String responseId = msg.getResponseId();
            // 是否是response
            if (BridgeUtil.isPresent(responseId)) {
                ValueCallback<String> function = responseCallbacks.get(responseId);
                String responseData = msg.getResponseData();
                function.onReceiveValue(responseData);
                responseCallbacks.remove(responseId);
            } else {
                ValueCallback<String> responseFunction;
                // if had callbackId
                final String callbackId = msg.getCallbackId();
                if (BridgeUtil.isPresent(callbackId)) {
                    responseFunction = new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String data) {
                            Message responseMsg = new Message();
                            responseMsg.setResponseId(callbackId);
                            responseMsg.setResponseData(data);
                            queueMessage(responseMsg);
                        }
                    };
                } else {
                    responseFunction = new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {

                        }
                    };
                }
                BridgeHandler handler;
                if (BridgeUtil.isPresent(msg.getHandlerName())) {
                    handler = messageHandlers.get(msg.getHandlerName());
                } else {
                    handler = defaultHandler;
                }
                if (handler != null) {
                    handler.handler(msg.getData(), responseFunction);
                }
            }
        }
    }

    public void loadUrl(String jsUrl, final ValueCallback<String> callback) {
        evaluateJavascript(jsUrl, callback);
        if (!BridgeUtil.useEvaluateJS()) {
            responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), callback);
        }
    }

    @Override
    public void evaluateJavascript(String script, ValueCallback<String> callback) {
        if (BridgeUtil.isBlank(script)) {
//            KLog.e("Script is Empty");
            return;
        }
        if (BridgeUtil.useEvaluateJS()) {
            super.evaluateJavascript(script, callback);
        } else {
            try {
                super.loadUrl(script);
            } catch (Exception e) {
//                KLog.e(e.getMessage());
            }
        }
    }

    /**
     * register handler,so that javascript can call it
     */
    public void registerHandler(String handlerName, BridgeHandler handler) {
        if (handler != null) {
            messageHandlers.put(handlerName, handler);
        }
    }

    /**
     * call javascript registered handler
     */
    public void callHandler(String handlerName, String data, ValueCallback<String> callBack) {
        doSend(handlerName, data, callBack);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE) {
                    progressbar.setVisibility(View.VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
