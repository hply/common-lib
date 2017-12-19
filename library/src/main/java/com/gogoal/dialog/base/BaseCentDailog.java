package com.gogoal.dialog.base;

import android.view.Gravity;

import cn.iyuxuan.library.R;

/**
 * author wangjd on 2017/4/25 0025.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public abstract class BaseCentDailog extends BaseDialog {
    @Override
    public int getDialogStyle() {
        return R.style.CenterDialog;
    }

    @Override
    public int gravity() {
        return Gravity.CENTER;
    }

}
