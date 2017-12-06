package com.gogoal.common.common;

import java.util.List;

/**
 * author wangjd on 2017/7/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :运行时权限用户处理回调
 */
public interface IPermissionListner {

    void onUserAuthorize();//授权

    void onRefusedAuthorize(List<String> deniedPermissions);//拒绝(拒绝的权限集合)
}
