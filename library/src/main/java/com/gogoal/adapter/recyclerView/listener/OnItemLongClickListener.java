package com.gogoal.adapter.recyclerView.listener;

import android.view.View;

import com.gogoal.adapter.recyclerView.BaseCommonAdapter;

/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(BaseCommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseCommonAdapter adapter, View view, int position) {
        onSimpleItemLongClick(adapter, view, position);
    }

    @Override
    public void onItemChildClick(BaseCommonAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseCommonAdapter adapter, View view, int position) {
    }

    public abstract void onSimpleItemLongClick(BaseCommonAdapter adapter, View view, int position);
}
