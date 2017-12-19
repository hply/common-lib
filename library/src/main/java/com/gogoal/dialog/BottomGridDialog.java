package com.gogoal.dialog;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gogoal.adapter.recyclerView.BaseCommonAdapter;
import com.gogoal.adapter.recyclerView.BaseViewHolder;
import com.gogoal.bean.BottomGridData;
import com.gogoal.common.OnItemClickListener;
import com.gogoal.dialog.base.BaseBottomDialog;

import java.util.ArrayList;

import cn.iyuxuan.library.R;

/**
 * @author wangjd on 2017/12/19 15:23.
 * @description :${annotated}.
 */
public class BottomGridDialog extends BaseBottomDialog {

    private OnItemClickListener<BottomGridData> listener;
    private TextView btnCancle;
    private static boolean themeNight;

    public static BottomGridDialog newInstance(int numColumns, ArrayList<BottomGridData> gridDatas, OnItemClickListener<BottomGridData> listener) {
        return newInstance(numColumns, gridDatas, listener, true);
    }

    public static BottomGridDialog newInstance(
            int numColumns,
            ArrayList<BottomGridData> gridDatas,
            OnItemClickListener<BottomGridData> listener,
            boolean themeNight) {
        BottomGridDialog dialog = new BottomGridDialog();
        dialog.listener = listener;
        Bundle bundle = new Bundle();
        bundle.putInt("num_columns", numColumns);
        bundle.putBoolean("theme_night", themeNight);
        bundle.putParcelableArrayList("grid_datas", gridDatas);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.dialog_bottom_grid;
    }

    @Override
    public void bindView(final View v) {
        themeNight = getArguments().getBoolean("theme_night");
        initView(v);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomGridDialog.this.dismiss();
            }
        });
        //Bundle 数据获取
        int numColumns = getArguments().getInt("num_columns");
        ArrayList<BottomGridData> gridDatas = getArguments().getParcelableArrayList("grid_datas");
//

        RecyclerView gridView = v.findViewById(R.id.dialog_grid_view);
        gridView.setLayoutManager(new GridLayoutManager(v.getContext(),
                numColumns, GridLayoutManager.VERTICAL, false));
        gridView.setAdapter(new BaseCommonAdapter<BottomGridData, BaseViewHolder>(R.layout.item_dialog_bottom_grid, gridDatas) {
            @Override
            protected void convert(final BaseViewHolder holder, final BottomGridData item) {
                holder.setText(R.id.tv_item_dialog_text, item.getItemText());
                holder.setTextColor(R.id.tv_item_dialog_text, themeNight ? 0XFF98A4B2 : 0xff333333);
                if (TextUtils.isEmpty(item.getImageUrl())) {
                    holder.setImageResource(R.id.iv_item_dialog_image, item.getImageRes());
                } else {
                    holder.setImageUrl(v.getContext(), R.id.iv_item_dialog_image, item.getImageUrl());
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(view, item, holder.getAdapterPosition());
                        }
                    }
                });
            }
        });
    }

    private void initView(View v) {
        //initView
        btnCancle = v.findViewById(R.id.btn_cancle);
        View dividerView = v.findViewById(R.id.view_divider);
        View rootLayout = v.findViewById(R.id.dialog_root_layout);
        //setTheme
        btnCancle.setTextColor(themeNight ? 0xffffffff : 0xff000000);
        rootLayout.setBackgroundColor(ContextCompat.getColor(v.getContext(),themeNight ?
                R.color.colorBackground_night :
                R.color.colorBackground_day));
        dividerView.setBackgroundColor(themeNight ? 0xff21232d : 0xffd9d9d9);
    }
}
