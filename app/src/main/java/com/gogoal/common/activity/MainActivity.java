package com.gogoal.common.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gogoal.bean.BottomGridData;
import com.gogoal.common.AppDevice;
import com.gogoal.common.ContentType;
import com.gogoal.common.FileUtils;
import com.gogoal.common.OnItemClickListener;
import com.gogoal.common.PathUtils;
import com.gogoal.common.R;
import com.gogoal.common.UFileUpload;
import com.gogoal.common.base.BaseActivity;
import com.gogoal.common.common.IPermissionListner;
import com.gogoal.common.view.SeekBar;
import com.gogoal.dialog.BottomGridDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ArrayList<BottomGridData> dataList;

    private SeekBar mSeekBar;

    private TextView textProgress;

    private TextView textFlag;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View mContentView) {
        mSeekBar = findViewById(R.id.seek_bar);
        textProgress = findViewById(R.id.tv_progress);
        textFlag = findViewById(R.id.tv_flag);
    }

    @Override
    public void doBusiness(final Context mContext) {
        dataList = new ArrayList<>();
        dataList.add(new BottomGridData("微信", R.mipmap.default_image));
        dataList.add(new BottomGridData("支付宝", R.mipmap.ic_launcher));
        dataList.add(new BottomGridData("QQ", R.mipmap.default_image));
        dataList.add(new BottomGridData("微博", R.mipmap.ic_launcher));
        dataList.add(new BottomGridData("易信", R.mipmap.default_image));
        dataList.add(new BottomGridData("facebook", R.mipmap.ic_launcher));

        findViewById(R.id.btn_dialog).setOnClickListener(new DemoClick(false));
        findViewById(R.id.btn_apk).setOnClickListener(new DemoClick(true));
        findViewById(R.id.btn_image).setOnClickListener(new DemoClick(true));
        findViewById(R.id.btn_image_null).setOnClickListener(new DemoClick(true));
        findViewById(R.id.btn_load_gallery).setOnClickListener(new DemoClick(false));
        findViewById(R.id.btn_upload_gogoal).setOnClickListener(new DemoClick(true));
        findViewById(R.id.btn_upload_zhitou).setOnClickListener(new DemoClick(true));
    }

    private class DemoClick implements View.OnClickListener {

        private boolean needPermission;

        private DemoClick(boolean needPermission) {
            this.needPermission = needPermission;
        }

        @Override
        public void onClick(final View v) {
            //需要存储权限的点击
            if (needPermission) {
                requestRuntimePermission(new IPermissionListner() {
                    @Override
                    public void onUserAuthorize() {
                        switch (v.getId()) {
                            case R.id.btn_apk:
                                Intent apkIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                apkIntent.setType(ContentType.APK);
                                apkIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(apkIntent, 10086);
                                break;
                            case R.id.btn_upload_gogoal:
                                Intent gogoalIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                gogoalIntent.setType(ContentType.APK);
                                gogoalIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(gogoalIntent, 147);
                                break;
                            case R.id.btn_upload_zhitou:
                                Intent zhitouIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                zhitouIntent.setType(ContentType.APK);
                                zhitouIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(zhitouIntent, 258);
                                break;
                            case R.id.btn_image:
                                Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                imgIntent.setType(ContentType.PNG);
                                imgIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(imgIntent, 10010);
                                break;
                            case R.id.btn_image_null:
                                Intent nullImgIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                                nullImgIntent.setType();
                                nullImgIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                startActivityForResult(nullImgIntent, 10011);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onRefusedAuthorize(List<String> deniedPermissions) {
                        Toast.makeText(v.getContext(), "需要获取存储权限", Toast.LENGTH_SHORT).show();
                    }
                }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            //无需存储权限的按钮点击
            else {
                switch (v.getId()) {
                    case R.id.btn_dialog:
                        BottomGridDialog.newInstance(4, dataList, new OnItemClickListener<BottomGridData>() {
                            @Override
                            public void onItemClick(View itemView, BottomGridData itemData, int position) {
                                Toast.makeText(v.getContext(), "pos=" + position + "::" + itemData.getItemText(), Toast.LENGTH_SHORT).show();
                            }
                        }).show(getSupportFragmentManager());
                        break;
                    case R.id.btn_load_gallery:
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            mSeekBar.setProgress(0);
            mSeekBar.setVisibility(View.VISIBLE);
            textFlag.setVisibility(View.VISIBLE);
            textProgress.setVisibility(View.VISIBLE);
            textFlag.setText("上传中...");
        } catch (Exception e) {
            //对象惨遭回收
            e.printStackTrace();
        }
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            //使用第三方应用打开
            String path = PathUtils.getPath(this, uri);
            Toast.makeText(MainActivity.this, path + " ", Toast.LENGTH_SHORT).show();
            switch (requestCode) {
                case 10086:
                    loadApk(path);
                    break;
                case 10010:
                    loadImage(path);
                    break;
                case 10011:
                    loadImage(path);
                    break;
                case 147:
                    loadZyyxProject(path, UFileUpload.Zyyx.GOGOAL);
                    break;
                case 258:
                    loadZyyxProject(path, UFileUpload.Zyyx.ZhiTou);
                    break;
                default:
                    break;
            }
        }
    }

    //上传图片
    private void loadImage(String path) {
        UFileUpload.getInstance().upload(new File(path),FileUtils.getFileExtension(new File(path)), new UFileUpload.UploadListener() {
            @Override
            public void onUploading(int progress) {
                mSeekBar.setProgress(progress);
                textProgress.setText(progress + "/100");
                if (progress == 100) {
                    textFlag.setText("分片合并中，请稍后...");
                }
            }

            @Override
            public void onSuccess(final String onlineUri) {
                textFlag.setText("已上传");
                Toast.makeText(MainActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                if (!isFinishing()) {
                    dialogUrl(onlineUri).show();
                }
            }

            @Override
            public void onFailed() {
                textFlag.setText("上传失败了");
                Toast.makeText(MainActivity.this, "上传失败了~", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("上传失败!")
                        .setMessage("是否重试？")
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findViewById(R.id.btn_image).performClick();
                            }
                        }).setNegativeButton("取消", null)
                        .setCancelable(false);
                if (!isFinishing()) {
                    builder.show();
                }

            }
        });
    }

    //上传apk文件
    private void loadApk(String path) {
        UFileUpload.getInstance().upload(new File(path), ContentType.APK, new UFileUpload.UploadListener() {
            @Override
            public void onUploading(final int progress) {
                mSeekBar.setProgress(progress);
                textProgress.setText(progress + "/100");
                if (progress == 100) {
                    textFlag.setText("分片合并中，请稍后...");
                }
            }

            @Override
            public void onSuccess(final String onlineUri) {
                Toast.makeText(MainActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                if (!isFinishing()) {
                    dialogUrl(onlineUri).show();
                }
                textFlag.setText("已上传");

            }

            @Override
            public void onFailed() {
                textFlag.setText("上传失败了");
                Toast.makeText(MainActivity.this, "上传失败了~", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("上传失败!")
                        .setMessage("是否重试？")
                        .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findViewById(R.id.btn_apk).performClick();
                            }
                        }).setNegativeButton("取消", null)
                        .setCancelable(false);
                if (!isFinishing()) {
                    builder.show();
                }
            }
        });
    }

    private AlertDialog.Builder dialogUrl(final String onlineUri) {
        return new AlertDialog.Builder(MainActivity.this)
                            .setTitle("上传成功")
                            .setMessage(onlineUri)
                            .setPositiveButton("复制URl", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AppDevice.copyTextToBoard(MainActivity.this, onlineUri);
                                }
                            }).setNegativeButton("取消", null)
                            .setCancelable(false);
    }

    public final void loadZyyxProject(String path,UFileUpload.Zyyx type){
        UFileUpload.getInstance().upload(new File(path), type, new UFileUpload.UploadListener() {
            @Override
            public void onUploading(int progress) {
                mSeekBar.setProgress(progress);
                textProgress.setText(progress + "/100");
                if (progress == 100) {
                    textFlag.setText("分片合并中，请稍后...");
                }
            }

            @Override
            public void onSuccess(String onlineUri) {
                if (!isFinishing()) {
                    dialogUrl(onlineUri).show();
                }
                textFlag.setText("已上传");
            }

            @Override
            public void onFailed() {
                Toast.makeText(MainActivity.this, "上传失败了", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
