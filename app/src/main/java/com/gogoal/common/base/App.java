package com.gogoal.common.base;

import com.gogoal.base.BaseApp;
import com.gogoal.common.UFileUpload;
import com.gogoal.common.common.AppConst;

/**
 * @author wangjd on 2017/12/5 0005.
 * @description :${annotated}.
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        UFileUpload.init(AppConst.PRIVATE_KEY,AppConst.PUBLIC_KEY);
    }

}
