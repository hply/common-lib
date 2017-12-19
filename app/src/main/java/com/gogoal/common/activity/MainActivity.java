package com.gogoal.common.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gogoal.bean.BottomGridData;
import com.gogoal.common.OnItemClickListener;
import com.gogoal.common.R;
import com.gogoal.common.base.BaseActivity;
import com.gogoal.common.common.IPermissionListner;
import com.gogoal.common.common.UFileUpload;
import com.gogoal.dialog.BottomGridDialog;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.iyuxuan.library.roundImage.RoundedImageView;

public class MainActivity extends BaseActivity {

    private RoundedImageView roundView;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View mContentView) {
        roundView = findViewById(R.id.riv);
    }

    @Override
    public void doBusiness(final Context mContext) {
        findViewById(R.id.btn_js_bridge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), JSBridgeActivity.class);
                intent.putExtra("web_url", "http://192.168.152.32:9000/#/meeting/detail");
                startActivity(intent);
            }
        });

        final ArrayList<BottomGridData> dataList = new ArrayList<>();
        dataList.add(new BottomGridData("微信", R.mipmap.default_image));
        dataList.add(new BottomGridData("支付宝", R.mipmap.ic_launcher));
        dataList.add(new BottomGridData("QQ", R.mipmap.default_image));
        dataList.add(new BottomGridData("微博", R.mipmap.ic_launcher));
        dataList.add(new BottomGridData("易信", R.mipmap.default_image));
        dataList.add(new BottomGridData("facebook", R.mipmap.ic_launcher));

        findViewById(R.id.btn_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomGridDialog.newInstance(4, dataList, new OnItemClickListener<BottomGridData>() {
                    @Override
                    public void onItemClick(View itemView, BottomGridData itemData, int position) {
                        Toast.makeText(mContext, "pos="+position+"::"+itemData.getItemText(), Toast.LENGTH_SHORT).show();
                    }
                }).show(getSupportFragmentManager());
            }
        });

        requestRuntimePermission(new IPermissionListner() {
            @Override
            public void onUserAuthorize() {
                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        File.separator + "mishop" + File.separator + "mishop_export_1512461696133.jpg");

                RequestOptions options = RequestOptions.noTransformation()
                        .circleCrop()
                        .placeholder(R.mipmap.logo)
                        .error(R.mipmap.logo);

                Glide.with(mContext).load(Uri.fromFile(file)).apply(options).into(roundView);

                //
                findViewById(R.id.btn_ufile).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!file.exists()) {
                            Toast.makeText(MainActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UFileUpload.getInstance().upload(file, UFileUpload.Type.IMAGE, new UFileUpload.UploadListener() {
                            @Override
                            public void onUploading(int progress) {
                                KLog.e(progress);
                            }

                            @Override
                            public void onSuccess(String onlineUri) {
                                KLog.e(onlineUri);
                            }

                            @Override
                            public void onFailed() {
                                KLog.e("上传失败");
                            }
                        });
                    }
                });

                //
                findViewById(R.id.btn_load_gallery).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }

            @Override
            public void onRefusedAuthorize(List<String> deniedPermissions) {
                Toast.makeText(mContext, "需要获取存储权限", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

}
