package com.gogoal.adapter.recyclerView.listener;

import android.view.View;

import com.gogoal.adapter.recyclerView.BaseCommonAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 * <p>
 * <p>
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 */
public abstract class OnItemClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(BaseCommonAdapter adapter, View view, int position) {
        onSimpleItemClick(adapter, view, position);
    }

    @Override
    public void onItemLongClick(BaseCommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseCommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseCommonAdapter adapter, View view, int position) {

    }

    public abstract void onSimpleItemClick(BaseCommonAdapter adapter, View view, int position);
}
