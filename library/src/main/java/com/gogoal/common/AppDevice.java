package com.gogoal.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.gogoal.base.BaseApp;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AppDevice {

    public static final int DPI480P = 480;
    public static final int DPI720P = 720;
    public static final int DPI1080P = 1080;

    // 手机网络类型
    private static final int NETTYPE_WIFI = 0x01;
    private static final int NETTYPE_CMWAP = 0x02;
    private static final int NETTYPE_CMNET = 0x03;

    private static Boolean _hasCamera = null;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    @Deprecated
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f * (dpValue >= 0 ? 1 : -1));
    }

    public static int dp2px(float dpValue) {
        final float scale = BaseApp.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f * (dpValue >= 0 ? 1 : -1));
    }


    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f * (pxValue >= 0 ? 1 : -1));
    }

    public static int px2dp(float pxValue) {
        final float scale = BaseApp.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f * (pxValue >= 0 ? 1 : -1));
    }

    public static int sp2px(Context ctx, float spValue) {
        final float scaledDensity = ctx.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaledDensity + 0.5f * (spValue >= 0 ? 1 : -1));
    }

    public static int sp2px(float spValue) {
        final float scaledDensity = BaseApp.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaledDensity + 0.5f * (spValue >= 0 ? 1 : -1));
    }

    /**
     * 显示度量
     */
    public static DisplayMetrics getDisplayMetrice(Context context) {
        if (context == null) {
            return null;
        }
        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if (wManager != null) {
            wManager.getDefaultDisplay().getMetrics(dm);
        }
        return dm;
    }

    /**
     * 获得屏幕宽度 px
     *
     * @return ;
     */
    @Deprecated
    public static int getWidth(Context context) {
        return getDisplayMetrice(context).widthPixels;
    }

    /**
     * 获得屏幕高度 px
     */
    @Deprecated
    public static int getHeight(Context context) {
        return getDisplayMetrice(context).heightPixels;
    }

    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static int getDefaultActionBarSize(Context mContext) {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        int[] attribute = new int[]{android.R.attr.actionBarSize};
        TypedArray array = mContext.obtainStyledAttributes(typedValue.resourceId, attribute);
        int titleHeight = array.getDimensionPixelSize(0 /* index */, -1 /* default size */);
        array.recycle();
        return titleHeight;
    }

    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 检测网络是否可用
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 跳转到应用详情设置页
     */
    public static void go2AppDetail(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }

    /**
     * 屏幕添加灰色蒙版
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0 --》不透明-全透明
        context.getWindow().setAttributes(lp);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 判断当前应用程序是否后台运行
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();

        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /*通过三原色调配*/
    public static int getColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * @param alpher      透明度,0.0d~1.0d
     * @param colorString 6位颜色值 ffaa25
     */
    private static String get16Alpher(double alpher, String colorString) {
        return new BigInteger(
                String.valueOf(
                        Math.round(alpher * 255)), 10)
                .toString(16) + colorString;
    }

    //获取真实的手机屏幕尺寸
    public static int[] getRealScreenSize(Activity activity) {
        int[] size = new int[2];
        int screenWidth = 0, screenHeight = 0;
        WindowManager w = activity.getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try {
                screenWidth = (Integer) Display.class.getMethod("getRawWidth")
                        .invoke(d);
                screenHeight = (Integer) Display.class
                        .getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        }
        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d,
                        realSize);
                screenWidth = realSize.x;
                screenHeight = realSize.y;
            } catch (Exception ignored) {
            }
        }
        size[0] = screenWidth;
        size[1] = screenHeight;
        return size;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        if (bmp == null) {
            return null;
        }
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        Bitmap bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, bmp.getWidth(), bmp.getHeight() - statusBarHeight);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bp;
    }

    @SuppressLint("HardwareIds")
    public static String getIMSI(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return "";
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            return telephonyManager.getSubscriberId();
        }
        return "";
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (telephonyManager != null) {
                return telephonyManager.getDeviceId();
            }
        }
        return "";
    }

    //相机是否可用
    public static boolean hasCamera(Context context) {
        if (_hasCamera == null) {
            PackageManager pckMgr = context.getPackageManager();
            boolean flag = pckMgr
                    .hasSystemFeature("android.hardware.camera.front");
            boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
            boolean flag2;
            flag2 = flag || flag1;
            _hasCamera = flag2;
        }
        return _hasCamera;
    }

    /**
     * 程序是否存在
     */
    public static boolean isPackageExist(Context context, String pckName) {
        try {
            PackageInfo pckInfo = context.getPackageManager()
                    .getPackageInfo(pckName, 0);
            if (pckInfo != null) {
                return true;
            }
        } catch (NameNotFoundException e) {
            e.getMessage();
        }
        return false;
    }

    /**
     * 是横屏？
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    /**
     * 是竖屏？
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    /**
     * 隐藏软键盘
     */
    protected void hideKeyBoard(Activity context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        boolean active = inputMethodManager.isActive();
        if (active) {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        View focusView = null;
        if (view instanceof EditText) {
            focusView = view;
        }

        if (focusView != null) {
            if (focusView.isFocused()) {
                focusView.clearFocus();
            }
            InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if (manager == null) {
                return;
            }
            manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            manager.hideSoftInputFromInputMethod(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            //强制隐藏键盘
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * 隐藏软键盘(更改弹起方式)
     */
    public static void hideSoftChangeMethod(View view) {
        Activity mActivity = null;
        if (view == null) {
            return;
        }
        View focusView = null;
        if (view instanceof EditText) {
            focusView = view;
        }
        Context context = view.getContext();
        if (context != null && context instanceof Activity) {
            mActivity = ((Activity) context);
            focusView = mActivity.getCurrentFocus();
        }

        if (focusView != null) {
            if (focusView.isFocused()) {
                focusView.clearFocus();
            }
            InputMethodManager manager = (InputMethodManager) focusView.getContext().getSystemService(INPUT_METHOD_SERVICE);
            if (manager == null) {
                return;
            }
            manager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            manager.hideSoftInputFromInputMethod(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if (null != mActivity) {
                mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            }
        }
    }

    /**
     * 显示软键盘--弹窗
     */
    public static void showSoftKeyboard(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    /**
     * 显示软键盘--VIEW
     */
    public static void showSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        if (!view.isFocusable()) {
            view.setFocusable(true);
        }
        if (!view.isFocusableInTouchMode()) {
            view.setFocusableInTouchMode(true);
        }
        if (!view.isFocused()) {
            view.requestFocus();
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            inputMethodManager.showSoftInput(view, 0);
            inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘--VIEW
     */
    public static void showSoftChangeMethod(View view) {
        Activity mActivity = null;
        if (view == null) {
            return;
        }
        if (!view.isFocusable()) {
            view.setFocusable(true);
        }
        if (!view.isFocusableInTouchMode()) {
            view.setFocusableInTouchMode(true);
        }
        if (!view.isFocused()) {
            view.requestFocus();
        }
        Context context = view.getContext();
        if (context != null && context instanceof Activity) {
            mActivity = ((Activity) context);
        }
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
            inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 0);
            if (null != mActivity) {
                Window window = mActivity.getWindow();
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        }
    }

    /**
     * 开关软键盘
     */
    public static void toogleSoftKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(
                INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.toggleSoftInput(0,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * SD卡是否可用
     */
    public static boolean isSdcardReady() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 获取当前的语言环境
     */
    public static String getCurCountryLan(Context context) {
        return context.getResources().getConfiguration().locale
                .getLanguage()
                + "-"
                + context.getResources().getConfiguration().locale
                .getCountry();
    }

    /**
     * 中文环境
     */
    public static boolean isZhCN(Context context) {
        String lang = context.getResources()
                .getConfiguration().locale.getCountry();
        if ("CN".equalsIgnoreCase(lang)) {
            return true;
        }
        return false;
    }

    /**
     * 设置全屏
     */
    public static void setFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 取消全屏
     */
    public static void cancelFullScreen(Activity activity) {
        WindowManager.LayoutParams params = activity.getWindow()
                .getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 获取程序的版本号
     *
     * @param context ;
     * @return ;
     */
    public static int getAppVersionCode(Context context) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 获取当前程序的版本名
     *
     * @param context ;
     * @return ;
     */
    public static String getAppVersionName(Context context) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return context.getPackageName();
    }


    /**
     * 获取程序的名字
     *
     * @param context  ;
     * @param packname ;
     * @return ;
     */
    public static String getAppName(Context context, String packname) {
        //包管理操作管理类
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(packname, 0);
            return info.loadLabel(pm).toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packname;
    }

    /**
     * 获取程序的签名
     *
     * @param context  ;
     * @param packname ;
     * @return ;
     */
    public static String getAppSignature(Context context, String packname) {
        try {
            //包管理操作管理类
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_SIGNATURES);
            //获取当前应用签名
            return packinfo.signatures[0].toCharsString();

        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return packname;
    }

    /**
     * 安装apk
     */
    public static void installAPK(Context context, File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    /**
     * 安装意图
     */
    public static Intent getInstallApkIntent(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * 安装App(支持7.0)
     *
     * @param filePath  文件路径
     * @param authority 7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
     *                  <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     */
    public static void installApp(Context context, final String filePath, final String authority) {
        installApp(context, getFileByPath(filePath), authority);
    }

    /**
     * 安装App（支持7.0）
     *
     * @param file      文件
     * @param authority 7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
     *                  <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     */
    public static void installApp(Context context, final File file, final String authority) {
        if (!isFileExists(file)) {
            Toast.makeText(context, "安装文件不存在！", Toast.LENGTH_SHORT).show();
            return;
        }
        context.startActivity(getInstallAppIntent(context, file, authority));
    }


    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(final String filePath) {
        return TextUtils.isEmpty(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(final File file) {
        return file != null && file.exists();
    }

    /**
     * 获取安装App(支持7.0)的意图
     *
     * @param context
     * @param file      文件
     * @param authority 7.0及以上安装需要传入清单文件中的{@code <provider>}的authorities属性
     *                  <br>参看https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     * @return intent
     */
    private static Intent getInstallAppIntent(Context context, final File file, final String authority) {
        if (file == null) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(context, authority, file);
        }
        intent.setDataAndType(data, type);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 到拨号盘
     */
    public static void openDial(Context context, String number) {
        if (number.contains(" ")) {
            number = number.replace(" ", "");
        }
        if (number.contains("-")) {
            number = number.replace("-", "");
        }

        Uri uri = Uri.parse("tel:" + number);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /***
     * 发信息
     */
    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     * @param context     上下文
     * @param packageName 判断程序的包名
     * @return : boolean
     * @Description : 这个包名的程序是否在运行
     * @Method_Name : isRunningApp
     */
    public static boolean isRunningApp(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                // find it, break
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 获取应用程序所有的非系统应用的包名
     */
    public static Map<String, String> getAppList(Context context) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        Map<String, String> appList = new HashMap<>();
        for (PackageInfo info : packages) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {//如果非系统应用，则添加至appList
                appList.put(info.applicationInfo.packageName, info.applicationInfo.loadLabel(context.getPackageManager()).toString());
            }
        }
        return appList;
    }

    public static void openApp(Context context, String packageName) {
        Map<String, String> appList = getAppList(context);
        if (appList.containsKey(packageName)) {
            Intent mainIntent = context.getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (mainIntent == null) {
                mainIntent = new Intent(packageName);
            } else {
            }
            context.startActivity(mainIntent);
        } else {
            Toast.makeText(context, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 启动指定的app中的Activity通知栏消息进入应用
     */
    public static boolean openAppActivity(Context context, String packageName,
                                          String activityName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, activityName);
        intent.setComponent(cn);
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断wifi是否打开
     */
    public static boolean isWifiOpen(Context context) {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // checkUpdate the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * 卸载App
     */
    public static void uninstallApk(Context context, String packageName) {
        if (isPackageExist(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                    packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    /**
     * 复制文本
     */
    public static void copyTextToBoard(Context context, String string) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", string);
        myClipboard.setPrimaryClip(myClip);
    }

    /**
     * 发送邮件
     *
     * @param context
     * @param subject 主题
     * @param content 内容
     * @param emails  邮件地址
     */
    public static void sendEmail(Context context, String subject,
                                 String content, String... emails) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            // 模拟器
            // intent.setType("text/plain");
            intent.setType("message/rfc822"); // 真机
            intent.putExtra(Intent.EXTRA_EMAIL, emails);
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, content);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasStatusBar(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 调用系统安装了的应用分享
     *
     * @param context
     * @param title
     * @param url
     */
    public static void showSystemShareOption(Activity context,
                                             final String title, final String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享：" + title);
        intent.putExtra(Intent.EXTRA_TEXT, title + " " + url);
        context.startActivity(Intent.createChooser(intent, "选择分享"));
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo)) {
                if ("cmnet".equals(extraInfo.toLowerCase())) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 禁止EditText输入空格 回车
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (" ".equals(source) || "\n\r".equals(source) || "\n".equals(source) || "\r".equals(source)) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    public static int getAudioDurition(Context context, String path) {

        MediaPlayer mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(context, Uri.fromFile(new File(path)));

            mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);

            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int duration = mediaPlayer.getDuration();

        mediaPlayer.release();
        mediaPlayer = null;

        return duration;
    }

    //去应用市场评价，下载
    public static void gotoMarket(Context context, String pck) {
        try {
            Uri uri = Uri.parse("market://details?id=" + pck);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "您的手机没有安装应用市场", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

//    public static void setTabLayoutWidth(TabLayout t, int dpMargin) {
//        try {
//            Class<?> tablayout = t.getClass();
//            Field tabStrip = tablayout.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//            LinearLayout layoutTab = (LinearLayout) tabStrip.get(t);
//            for (int i = 0; i < layoutTab.getChildCount(); i++) {
//                View child = layoutTab.getChildAt(i);
//                child.setPadding(0, 0, 0, 0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//
//                params.setMargins(
//                        AppDevice.dp2px(dpMargin),
//                        0,
//                        AppDevice.dp2px(dpMargin),
//                        0);
//
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (Exception e) {
//            e.getMessage();
//        }
//    }

}