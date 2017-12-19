package com.gogoal.common;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * SharedPreferences
 */
public class SPTools {

    private static final String PREFERENCES_KEY = "hply_preferences";

    private static SharedPreferences mPreference = null;

    private static boolean isInited = false;

    public synchronized static void initSharedPreferences(Context context) {
        if (mPreference == null) {
            mPreference = context.getSharedPreferences(PREFERENCES_KEY,
                    Context.MODE_PRIVATE);
        }
        isInited = true;
    }

    public static void saveArrayList(String key, LinkedHashSet<String> list) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putStringSet(key, list);
        editor.apply();
    }

    public static LinkedHashSet<String> getArrayList(String key) {
        Set<String> stringSet = mPreference.getStringSet(key, new LinkedHashSet<String>());
        return new LinkedHashSet<>(stringSet);
    }

    public static boolean isSpInited() {
        return isInited;
    }

    //
    public static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mPreference.getBoolean(key, defaultValue);
    }

    //
    public static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLong(String key, long defaultValue) {
        return mPreference.getLong(key, defaultValue);
    }

    //
    public static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(String key, int defaultValue) {
        return mPreference.getInt(key, defaultValue);
    }

    //
    public static void saveFloat(String key, float value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float getFloat(String key, float defaultValue) {
        return mPreference.getFloat(key, defaultValue);
    }

    public static boolean containKey(String key) {
        return mPreference.contains(key);
    }

    //
    public static String saveString(String key, String value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key, value);
        editor.apply();
        return key;
    }

    public static String getString(String key, String defaultValue) {
        return mPreference.getString(key, defaultValue);
    }

    //
    public static void saveSetData(String key, Set<String> siteno) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putStringSet(key, siteno);
        editor.apply();
    }

    public static LinkedHashSet<String> getSetData(String key, Set<String> defSet) {
        return new LinkedHashSet<>(mPreference.getStringSet(key, defSet));
    }

    //
    public static void clear() {
        mPreference.edit().clear().apply();
    }

    //
    public static void clearItem(String key) {
        mPreference.edit().remove(key).apply();
    }

////======================================================================
//
//    /**
//     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
//     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
//     *
//     * @param object 待加密的转换为String的对象
//     * @return String   加密后的String
//     */
//    private static String Object2String(Object object) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        ObjectOutputStream objectOutputStream;
//        try {
//            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
//            objectOutputStream.writeObject(object);
//            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
//            objectOutputStream.close();
//            return string;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 使用Base64解密String，返回Object对象
//     *
//     * @param objectString 待解密的String
//     * @return object      解密后的object
//     */
//    private static Object String2Object(String objectString) {
//        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
//        ObjectInputStream objectInputStream;
//        try {
//            objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            Object object = objectInputStream.readObject();
//            objectInputStream.close();
//            return object;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }
//
//    /**
//     * 使用SharedPreference保存对象
//     *
//     * @param key        储存对象的key
//     * @param saveObject 储存的对象
//     */
//    public static void saveSPObject(String key, Object saveObject) {
//        SharedPreferences.Editor editor = mPreference.edit();
//        String string = Object2String(saveObject);
//        editor.putString(key, string);
//        editor.apply();
//    }
//
//    /**
//     * 获取SharedPreference保存的对象
//     *
//     * @param key 储存对象的key
//     * @return object 返回根据key得到的对象
//     */
//    public static Object getSPObject(String key) {
//        String string = mPreference.getString(key, null);
//        if (string != null) {
//            Object object = String2Object(string);
//            return object;
//        } else {
//            return null;
//        }
//    }
}
