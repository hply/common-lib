package com.gogoal.common.base;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.gogoal.base.AppManager;
import com.gogoal.common.common.IPermissionListner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjd on 2017/12/5 0005.
 * @description :${annotated}.
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {

    private static IPermissionListner mListener;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View inflate = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(inflate);
        AppManager.getInstance().addActivity(this);
        initView(inflate);
        doBusiness(this);

    }

    //封装运行时权限
    public static void requestRuntimePermission(IPermissionListner listener, String... permissions) {
        mListener = listener;
        List<String> permissionList = new ArrayList<>();

        Activity activity = AppManager.getInstance().currentActivity();

        if (activity == null) {
            return;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            mListener.onUserAuthorize();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<String> deniedPermissionList = new ArrayList<>();
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permissions[i]);
                            break;
                        }
                    }
                    if (deniedPermissionList.isEmpty()) {
                        mListener.onUserAuthorize();
                    } else {
                        mListener.onRefusedAuthorize(deniedPermissionList);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }
}
