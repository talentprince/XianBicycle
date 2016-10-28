# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
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
# -dontobfuscate
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify

-dontwarn **
-optimizationpasses 5
-repackageclasses 'org.weyoung'

-keepattributes SourceFile,LineNumberTable
-keepattributes Signature,*Annotation*
-keep class sun.misc.Unsafe { *; }

-keep class com.baidu.** { *; }
-keep class com.tencent.** { *; }

#Keep ButterKnife inject
-keep class **$$ViewBinder { *; }
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#Keep Model
-keep class org.weyoung.xianbicycle.data.** { *; }

-keep class org.weyoung.xianbicycle.NavigatorActivity { *; }

#Keep Gson
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }

#Keep OkHttp
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

#Keep RxJava
-dontwarn sun.misc.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

#-Keep the the fields annotated with @Inject of any class that is not deleted.
-keepclassmembers class * {
  @javax.inject.* <fields>;
}

#-Keep the names of classes that have fields annotated with @Inject and the fields themselves.
-keepclasseswithmembernames class * {
  @javax.inject.* <fields>;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembers class ** {
    public void onEvent*(**);
}

#gaode
-keep class com.amap.api.location.**{*;}

-keep class com.amap.api.fence.**{*;}

-keep class com.autonavi.aps.amapapi.model.**{*;}