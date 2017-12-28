package com.gogoal.common.common;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author wangjd on 2017/8/24 0024.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 * <p>
 * js交互
 */
public class WebViewUtil {

    private Context mContext;

    public WebViewUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 读取raw文件夹中文本或超文本内容为字符串
     */
    public String getRawString(int rawId) {
        InputStream in = mContext.getResources().openRawResource(rawId);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
