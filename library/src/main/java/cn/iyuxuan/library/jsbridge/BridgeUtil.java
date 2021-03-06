package cn.iyuxuan.library.jsbridge;

import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.StringCharacterIterator;
import java.util.List;

class BridgeUtil {
    final static String YY_OVERRIDE_SCHEMA = "jsbridge://";
    final static String YY_RETURN_DATA = YY_OVERRIDE_SCHEMA + "return/";//格式为   yy://return/{function}/returncontent
    final static String YY_BRIDGE_LOADED = YY_OVERRIDE_SCHEMA + "__bridge_loaded__/";
    private final static String YY_FETCH_QUEUE = YY_RETURN_DATA + "_fetchQueue/";
    private final static String EMPTY_STR = "";
    final static String UNDERLINE_STR = "_";
    private final static String SPLIT_MARK = "/";
    private final static String JAVASCRIPT_STR = "javascript:";

    final static String CALLBACK_ID_FORMAT = "JAVA_CB_%s";
    final static String JS_HANDLE_MESSAGE_FROM_JAVA = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    final static String JS_FETCH_QUEUE_FROM_JAVA = "javascript:WebViewJavascriptBridge._fetchQueue();";

    static String parseFunctionName(String jsUrl) {
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
    }

    static boolean useEvaluateJS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    static String getDataFromReturnUrl(String url) {
        if (url.startsWith(YY_FETCH_QUEUE)) {
            return url.replace(YY_FETCH_QUEUE, EMPTY_STR);
        }

        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);

        if (functionAndData.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < functionAndData.length; i++) {
                sb.append(functionAndData[i]);
            }
            return sb.toString();
        }
        return null;
    }

    static String getFunctionFromReturnUrl(String url) {
        String temp = url.replace(YY_RETURN_DATA, EMPTY_STR);
        String[] functionAndData = temp.split(SPLIT_MARK);
        if (functionAndData.length >= 1) {
            return functionAndData[0];
        }
        return null;
    }

    /**
     * 将本地js文件注入为第一个script引用
     */
    static void webViewLoadLocalJs(WebView view, String path) {
        String jsContent = assetFile2Str(view.getContext(), path);
        view.loadUrl(JAVASCRIPT_STR + jsContent);
    }

    private static String assetFile2Str(Context c, String urlStr) {
        InputStream in = null;
        try {
            in = c.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);

            bufferedReader.close();
            in.close();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        }
        return null;
    }

    private static boolean isPresent(List list) {
        return list != null && list.size() > 0;
    }

    static boolean isPresent(String string) {
        return string != null && string.length() > 0;
    }

    static boolean isBlank(List list) {
        return !isPresent(list);
    }

    static boolean isBlank(String string) {
        return !isPresent(string);
    }

    static String escapeJsonString(String jsonString) {
        final StringBuilder result = new StringBuilder();
        StringCharacterIterator iterator = new StringCharacterIterator(jsonString);
        char character = iterator.current();
        while (character != StringCharacterIterator.DONE) {
            if (character == '\"') {
                result.append("\\\"");
            } else if (character == '\\') {
                result.append("\\\\");
            } else if (character == '/') {
                result.append("\\/");
            } else if (character == '\b') {
                result.append("\\b");
            } else if (character == '\f') {
                result.append("\\f");
            } else if (character == '\n') {
                result.append("\\n");
            } else if (character == '\r') {
                result.append("\\r");
            } else if (character == '\t') {
                result.append("\\t");
            } else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
}
