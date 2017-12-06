package com.gogoal.common.common;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.iyuxuan.library.imagepicker.loader.ImageLoader;

/**
 * author wangjd on 2017/2/14 0014.
 * Staff_id 1375
 * phone 18930640263
 */

public class UILImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(Uri.fromFile(new File(path))).into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }
}
