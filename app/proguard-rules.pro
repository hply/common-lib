# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-dontpreverify           # 混淆时是否做预校验
-verbose                # 混淆时是否记录日志

#-dontusemixedcaseclassnames

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

###############################common-lib####################################
-dontwarn  com.gogoal.dialog.**

-dontwarn com.gogoal.adapter.recyclerView.**
-keep public class * extends com.gogoal.adapter.recyclerView.BaseCommonAdapter
-keep public class * extends com.gogoal.adapter.recyclerView.BaseViewHolder
-keepclassmembers  class **$** extends com.gogoal.adapter.recyclerView.BaseViewHolder {
     <init>(...);
}
#=================Glide============================================#
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.**

################common###############
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}
-keep class com.gogoal.investment2c.bean.** { *; }
-keep class android.support.design.widget.** {*;}
-keep class android.support.v4.view.**{ *;}
-keep class android.support.v7.view.**{ *;}
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment

-dontwarn android.support.**
-dontwarn com.tencent.**
-dontwarn org.dom4j.**
-dontwarn org.slf4j.**
-dontwarn org.http.mutipart.**
-dontwarn org.apache.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn org.apache.commons.codec.binary.**

-dontwarn android.util.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * extends android.support.v7.app.AppCompatActivity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-dontwarn android.support.v4.**
-dontwarn android.support.v7.**
-dontwarn android.support.v4.view.**
-dontwarn android.support.v7.view.**

################fastjson##################
-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**

-keepattributes Signature

################butterknife##################
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keep class com.gogoal.common.ContentType{*; }

#######################微信######################
-dontwarn com.tencent.**
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*; }
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*; }

-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.wxop.** { *; }

################EvenBus###############
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}
-keepattributes *Annotation*

#====================OKHTTP===============================#
#okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.**{*;}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#Retrofit
-dontwarn javax.annotation.**
-dontwarn org.codehaus.**
-dontwarn java.nio.**
-dontwarn java.lang.invoke.**
#################webview###############

-keep class com.gogoal.lib.base.BaseActivity{public *;}

-keepattributes *JavascriptInterface*

-keepattributes *Annotation*,*Exceptions*,Signature
-keepattributes EnclosingMethod

##########################RxJava&RXAndroid#############################
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}