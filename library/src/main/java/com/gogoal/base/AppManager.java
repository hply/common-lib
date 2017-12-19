package com.gogoal.base;

import android.app.Activity;
import android.content.Context;

import com.gogoal.common.ArrayUtils;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppManager {

    private static LinkedList<Activity> activityStack;

    private volatile static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (null == instance) {
            synchronized (AppManager.class) {
                if (null == instance) {
                    instance = new AppManager();
                    activityStack = new LinkedList<>();
                }
            }
        }

        return instance;
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null && !activityStack.isEmpty())
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 添加Activity到堆栈
     */
    void addActivity(Activity activity) {
        activityStack.remove(activity);
        activityStack.add(activity);
    }

    private boolean isEmpty(Activity activity) {
        return ArrayUtils.isEmpty(activityStack) || activity == null;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (ArrayUtils.isEmpty(activityStack)) {
            return null;
        }
        return activityStack.get(activityStack.size() - 1);
    }

    private void removeActivity(Activity activity) {
        if (activity != null && activityStack.contains(activity)) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (!isEmpty(activity)) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (!ArrayUtils.isEmpty(activityStack)) {
            for (Iterator<Activity> iterator = activityStack.iterator(); iterator.hasNext(); ) {
                Activity a = iterator.next();
                if (a.getClass().getSimpleName().equalsIgnoreCase(cls.getSimpleName())) {
                    iterator.remove();
                    a.finish();
                }
            }
        }
    }

    /**
     * 结束除当前Activity之外的Activity
     */

    public void finishBackActivity(Context activity) {
        if (!ArrayUtils.isEmpty(activityStack)) {
            for (Iterator<Activity> iterator = activityStack.iterator(); iterator.hasNext(); ) {
                Activity a = iterator.next();
                if (!a.getClass().getSimpleName().equalsIgnoreCase(activity.getClass().getSimpleName())) {
                    iterator.remove();
                    a.finish();
                }
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (!ArrayUtils.isEmpty(activityStack)) {
            for (Iterator<Activity> iterator = activityStack.iterator(); iterator.hasNext(); ) {
                Activity a = iterator.next();
                iterator.remove();
                a.finish();
            }
        }
    }

    /**
     * 判断指定Activity是否在栈中
     */
    public static boolean iaActivityInSatck(Class<?> activityClass) {
        if (!ArrayUtils.isEmpty(activityStack)) {
            for (Activity activity : activityStack) {
                if (activity.getClass().getSimpleName().equalsIgnoreCase(activityClass.getSimpleName())) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

}
