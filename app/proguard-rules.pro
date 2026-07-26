# BarbaBypass ProGuard Rules
# 保留 Xposed 模块入口点
-keep class com.barbabypass.skip.** { *; }
-keep interface com.barbabypass.skip.** { *; }

# 保留 xposed.dat 注册表不被混淆
-dontwarn de.robv.android.xposed.**
-keep class de.robv.android.xposed.** { *; }

# 保留反射访问的广告类（运行时存在，编译时不存在）
-dontwarn com.noah.api.**
-dontwarn com.uc.*
-dontwarn com.quark.*
-dontwarn com.alipay.*
