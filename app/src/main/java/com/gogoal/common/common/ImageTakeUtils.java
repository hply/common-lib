package com.gogoal.common.common;

import android.content.Context;
import android.content.Intent;

import com.gogoal.common.activity.TakePhotoActivity;

import cn.iyuxuan.library.imagepicker.ITakePhoto;

/**
 * author wangjd on 2017/2/15 0015.
 * Staff_id 1375
 * phone 18930640263
 */
public class ImageTakeUtils {

    private ITakePhoto listener;

    private ImageTakeUtils() {
    }

    private static ImageTakeUtils instance = null;

    public static ImageTakeUtils getInstance() {
        if (instance == null) {
            synchronized (ImageTakeUtils.class) {
                if (instance == null) {
                    instance = new ImageTakeUtils();
                }
            }
        }
        return instance;
    }

    public void takePhoto(Context context, int limit, boolean canCrop, ITakePhoto takePhotoListener){
        takePhoto(context,limit,canCrop,1000,takePhotoListener);
    }

    public void takePhoto(Context context, int limit, boolean canCrop, int canCropSize, ITakePhoto takePhotoListener){
        this.listener=takePhotoListener;
        Intent intent=new Intent(context, TakePhotoActivity.class);
        intent.putExtra("limit",limit);
        intent.putExtra("canCrop",canCrop);
        intent.putExtra("canCropSize",canCropSize);
        context.startActivity(intent);
    }

    public ITakePhoto getListener() {
        return listener;
    }
}
