# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/applemac/tools/Android/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

#google
-keep class com.google.** { *; }

-keep class com.google.gson.** { *; }

#极光推送
-keep class cn.jpush.android.** { *; }

-keep class com.viewpagerindicator.** { *; }

-keep class com.loopj.android.** { *; }

#shuba
-keep class com.qiwenge.android.models.** { *; }
-keep class com.qiwenge.android.models.base.** { *; }
-keep class com.qiwenge.android.utils.http.** { *; }
-keep class com.qiwenge.android.async.** { *; }
-keep class com.qiwenge.android.ui.** { *; }
-keep class com.qiwenge.android.adapters.** { *; }