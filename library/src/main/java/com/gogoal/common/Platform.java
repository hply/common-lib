package com.gogoal.common;

import android.os.Build;

/**
 * @author wangjd on 2018/01/03 16:06.
 * @description :${annotated}.
 */
public class Platform {
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
