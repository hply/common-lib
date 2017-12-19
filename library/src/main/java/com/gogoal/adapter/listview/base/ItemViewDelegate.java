package com.gogoal.adapter.listview.base;

import com.gogoal.adapter.listview.ViewHolder;

public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}
