package com.gogoal.common.base;

import android.app.Application;
import android.content.Context;

/**
 * @author wangjd on 2017/12/5 0005.
 * @description :${annotated}.
 */
public class App extends Application {

    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Context getAppContext(){
        return app.getApplicationContext();
    }
}
