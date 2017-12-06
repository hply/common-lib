package com.gogoal.common.base;

import android.content.Context;
import android.view.View;

/**
 * @author wangjd on 2017/12/5 0005.
 * @description :${annotated}.
 */
public interface IBase {

    /**
     * 绑定视图
     */
    int bindLayout();

    /**
     * 初始化找控件
     */
    void initView(final View mContentView);

    /**
     * 业务逻辑
     */
    void doBusiness(final Context mContext);
}
