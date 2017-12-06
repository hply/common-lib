package com.gogoal.common.base;

import android.app.Activity;

import com.gogoal.common.common.ArrayUtils;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author wangjd on 2017/12/5 0005.
 * @description :${annotated}.
 */
public class AppManager {

    private volatile static AppManager instance;

    private AppManager() {
        activityStack = new LinkedList<>();
    }

    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (null == instance) {
            synchronized (AppManager.class) {
                if (null == instance) {
                    instance = new AppManager();
                }
            }
        }
        return instance;
    }

    //
    private LinkedList<Activity> activityStack;

    /**
     * 判断指定Activity是否在栈中
     */
    public boolean iaActivityInSatck(Class<?> activityClass) {
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

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (!ArrayUtils.isEmpty(activityStack)) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        }
        return null;
    }

    /**
     * 添加Activity到堆栈
     */
    void addActivity(Activity activity) {
        removeActivity(activity);
        activityStack.add(activity);
    }

    void removeActivity(Activity activity) {
        if (ArrayUtils.isEmpty(activityStack) || activity == null) {
            return;
        }
        activityStack.remove(activity);
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

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (!ArrayUtils.isEmpty(activityStack)
                && activity != null
                && activityStack.contains(activity)) {
            removeActivity(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<? extends Activity> cls) {
        if (!ArrayUtils.isEmpty(activityStack) && cls != null) {
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

    public void finishBackActivity(Activity activity) {
        if (!ArrayUtils.isEmpty(activityStack) && activity != null) {
            for (Iterator<Activity> iterator = activityStack.iterator(); iterator.hasNext(); ) {
                Activity a = iterator.next();
                if (!a.getClass().getSimpleName().equalsIgnoreCase(activity.getClass().getSimpleName())) {
                    a.finish();
                    iterator.remove();
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
                a.finish();
                iterator.remove();
            }
        }
    }
}
