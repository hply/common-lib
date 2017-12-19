package com.gogoal.base;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.gogoal.common.AppDevice;

/**
 * @author wangjd on 2017/12/19 13:10.
 * @description :${annotated}.
 */
public class BaseApp extends Application {

    /*通用常量*/
    public static int SCREEN_HEIGHT, SCREEN_WIDTH, STATUSBAR_HEIGHT;
    public static boolean isLowDpi;

    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        getDeviceInfo(this);
    }

    /*获取设备信息*/
    private void getDeviceInfo(Context appContext) {
        DisplayMetrics displayMetrice = AppDevice.getDisplayMetrice(appContext);
        //屏幕高度
        SCREEN_HEIGHT = displayMetrice.heightPixels;
        //屏幕宽度
        SCREEN_WIDTH = displayMetrice.widthPixels;
        // 获得状态栏高度
        STATUSBAR_HEIGHT = appContext
                .getResources()
                .getDimensionPixelSize(
                        appContext.getResources().getIdentifier(
                                "status_bar_height", "dimen", "android"));
        isLowDpi = SCREEN_WIDTH < 720;
    }

    public static Context getAppContext(){
        return app.getApplicationContext();
    }
}
