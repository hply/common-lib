package com.gogoal.adapter.listview;

import android.content.Context;

import com.gogoal.adapter.listview.base.ItemViewDelegate;

import java.util.List;

public abstract class BaseCommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public BaseCommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                BaseCommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);

}
