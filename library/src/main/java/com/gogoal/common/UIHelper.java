package com.gogoal.common;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.gogoal.base.BaseApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author wangjd on 2017/8/1 0001.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class UIHelper {

    public static <T extends View> T findViewById(View parentView, @IdRes int childId) {
        return (T) parentView.findViewById(childId);
    }

    /**
     * 读取raw文件夹中文本或超文本内容为字符串
     */
    public static String getRawString(int rawId) {
        InputStream in = BaseApp.getAppContext().getResources().openRawResource(rawId);
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

    /**
     * 获取String资源文本
     */
    public static String getString(@StringRes int stringId) {
        return BaseApp.getAppContext().getString(stringId);
    }

    /**
     * 获取颜色资源
     */
    public static @ColorInt
    int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(BaseApp.getAppContext(), colorRes);
    }

    /**
     * 获取mipmap、drawable的资源Drawable
     */
    public static Drawable getDrawable(@DrawableRes int drawableRes) {
        return ContextCompat.getDrawable(BaseApp.getAppContext(), drawableRes);
    }
}
