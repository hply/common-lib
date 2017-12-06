package cn.iyuxuan.library.jsbridge;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * author wangjd on 2017/1/17 0017.
 * Staff_id 1375
 * phone 18930640263
 */
public class Utils {

    private static DisplayMetrics getDms(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int dp2px(Context context,int dpValue){
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (density*dpValue+0.5);
    }

}
