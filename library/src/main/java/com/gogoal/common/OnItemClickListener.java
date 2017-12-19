package com.gogoal.common;

import android.view.View;

/**
 * @author wangjd on 2017/12/19 15:48.
 * @description :${annotated}.
 */
public interface OnItemClickListener<T> {

    /**
     * 自定义的接口方法，方便自定义点击事件处理
     */
    void onItemClick(View itemView, T itemData, int position);
}
