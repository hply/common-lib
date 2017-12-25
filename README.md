本类库是关于朝阳永续android项目中使用到的开源项目或者常用项目的二次封装工具类的整合（王结东）

    目前beta版本分为四大模块————

    1.jsbridge:由曹郁剑王结东黄舜2016年基于https://github.com/lzyzsd/JsBridge的JsBridge修改
                  具体使用实例见智投项目和黑版GoGoal App

    2.roundImage：圆形或者圆角矩形的控件库，和Glide完美兼容
                    支持圆形riv_oval=true
                    支持圆角矩形riv_oval=false,riv_corner_radius可以设置角度
                    支持边框riv_border_width="2dp"宽度设置和riv_border_color颜色设置

    3.UFile:UCloud旗下对象云存储，主要是sdk部分
            key的初始化和上传工具类不在本类库中，详情使用查看智投项目和黑版GoGoal App中的UFileUpload

    4.Common模块： 1) adapter集成了hongyang大神的适配器和参考思路写了recyclerView的适配器;
                   2) base：包含BaseApp,继承自Application,所以主项目的APP类继续BaseApp即可，
                            里面包含了设备的常用常量的获取。
                            但是切记：要在清单配置文件中申明AndroidManifest.xml
                            JsonEngine：json解析的引擎，根据具体项目继承实现两个方法即可，用途不大
                            AppManager：Activity栈的管理类，在项目的BaseActivity的onCreat中添加，
                                        onDestry中移除即可
                   3) dialog:基于DialogFragment的简易封装，能轻松实现任何弹窗，支持黑白两个主题
                             顶部的弹窗继承BaseTopDialog、中间的BaseCentDailog；
                             底部的继承BaseBottomDialog，本质Fragment可以和组件通信发消息
                             注意，无论使用哪一个，记得show；
                   4) AppDevice类：dp/sp/px的转换，屏幕宽高的获取（已经在BaseApp中了），显示密度
                                   检测网络是否可用                             —— isNetworkConnected
                                   判断wifi是否打开                             —— isWifiOpen
                                   获取当前网络类型                             —— getNetworkType
                                   跳转到应用市场                               ——  gotoMarket
                                   Activity添加灰色蒙版                         —— backgroundAlpha
                                   判断当前应用程序是否后台运行                   —— isBackground
                                   获取真实的手机屏幕尺寸(含虚拟导航栏,状态栏)     —— getRealScreenSize
                                   获取当前屏幕截图，不包含状态栏                 —— snapShotWithoutStatusBar
                                   获取设备IMSI                                 —— getIMSI
                                   获取设备IMEI                                 —— getIMEI
                                   相机是否可用                                  —— hasCamera
                                   程序是否存在                                  —— isPackageExist
                                   横竖屏判断                                    —— isLandscape/isPortrait
                                   隐藏/显示软键盘                               —— hideKeyBoard/showSoftKeyboard
                                   SD卡是否可用                                  —— isSdcardReady
                                   手机的语言环境                                —— isZhCN
                                   设置/取消全屏                                 —— setFullScreen/cancelFullScreen
                                   获取程序的指纹签名                             —— getAppSignature
                                   安装App                                       —— installApp
                                   卸载App                                       —— uninstallApk
                                   拨打电话(携带号码到拨号盘,不建议直接拨打,会被国内系统禁用) —— openDial
                                   复制文本到设备剪切板                           —— copyTextToBoard
                                   获取应用程序所有的非系统应用的包名               —— getAppList
                                   打开指定包名的App                              —— openApp
                                   启动指定的app中的Activity通知栏消息进入应用      —— openAppActivity
                   5) ArrayUtils类：集合辅助类
                                    isEmpty:对象是否为空或者空集
                                    appendElements：String的集合拼接成串，分号隔开
                   6) ExceptionUtils:主动抛异常

                   7) ImageLoader :基于Glide图片加载的简单封装

                   8) Spanny SpannableStringBuilder的封装使用

                   9) SPTools SharedPreferences的封装使用，需要在Application中初始化,已在BaseApp中初始化，继承BaseApp

                   10) StringUtils : 判断一个字符是否是中文            ——        isChinese(char c)
                                     是否含有中文                      ——        isChinese(String str)
                                     字符串去掉空格回车换行制表位      ——        replaceBlank
                                     字符串半角化                      ——        toDBC
                                     字符串全角化                      ——        toSBC
                                     url进行utf-8编码                  ——        decodeUrl
                                     字符串反转                        ——        reverseString
                                     绝对空(空串，空String对象,null)   ——        isActuallyEmpty
                                     输入的手机号是否合法              ——       checkPhoneString
                                     输入的邮箱格式是否合法            ——       checkEmail
                                     输入的身份证号码是否合法          ——          checkIdentity
                                     获取非空字符串                    ——        getNotNullString
                                     获取Double                        ——        parseStringDouble
                                     随机数                            ——        getRandomString

                    11)

