package com.gogoal.common;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import cn.iyuxuan.library.R;

/**
 * @author wangjd on 2017/12/20 10:11.
 * @description :${annotated}.
 */
public class ImageLoader {

    /**
     * @param context 上下文，可以是Activiy Fragment 和普通Context;
     * @param image   资源Id，String图片url;
     * @param view    图片控件
     * @param options 配置
     */
    public static <T, View extends ImageView> void loadImage(Object context, T image, View view, RequestOptions options) {
        RequestOptions apply = RequestOptions.noAnimation();
        RequestManager manager = null;
        if (options != null) {
            apply = apply.apply(options);
        }
        if (context instanceof Activity) {
            manager = Glide.with((Activity) context);
        } else if (context instanceof Fragment) {
            manager = Glide.with((Fragment) context);
        } else if (context instanceof Context) {
            manager = Glide.with((Context) context);
        } else {
            ExceptionUtils.throwException("context 必须是Activity、Fragment、Context");
        }
        manager.load(image).apply(apply).into(view);
    }

    /**
     * @param context 上下文，可以是Activiy Fragment 和普通Context;
     * @param image   资源Id，String图片url;
     * @param view    图片控件
     */
    public static <T, View extends ImageView> void loadImage(Object context, T image, View view) {
        RequestOptions apply = RequestOptions.noAnimation()
                .centerCrop()
                .placeholder(R.mipmap.default_image);
        loadImage(context, image, view, apply);
    }
}
