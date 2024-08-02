-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);

}

-keep class androidx.arch.core.** { *; }

# 如果引入了Camerax库请添加混淆
-keep class com.luck.lib.camerax.** { *; }

# 如果引入了Ucrop库请添加混淆
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#Glide混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Gson
-keep class com.google.gson.reflect.TypeToken
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type

# XPopup混淆
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.** { *;}
-dontwarn com.old_lxj.xpopup.widget.**
-keep class com.old_lxj.xpopup.widget.** { *;}

# MVVM架构
-keep class me.hgj.jetpackmvvm.**{*;}
################ ViewBinding & DataBinding & ViewModel ###############
-keep class * extends androidx.lifecycle.ViewModel{*;}
-keep public interface androidx.viewbinding.ViewBinding
-keep class * implements androidx.viewbinding.ViewBinding{*;}
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static * inflate(android.view.LayoutInflater);
  public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
  public static * bind(android.view.View);
}

# 图片选择https://github.com/LuckSiege/PictureSelector/blob/version_component/README_CN.md
-keep class com.luck.picture.lib.** { *; }
#如果引入了Camerax库请添加混淆
-keep class com.luck.lib.camerax.** { *; }
#如果引入了Ucrop库请添加混淆
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

# Firebase Crashlytics
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

# Firebase
-keep class com.google.firebase.** {*;}
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

#饺子播放器
-keep public class cn.jzvd.JZMediaSystem {*; }
-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }

-keep class com.nmk.myapplication.work.date.** { *; }