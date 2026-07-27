package com.barbabypass.skip;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 * BarbaBypass - 芭芭农场广告绕过模块
 *
 * 通过多层 Hook 策略拦截广告播放流程，在任意时机触发奖励回调。
 * 策略之间相互独立，任一生效即可获得奖励。
 *
 * @author BarbaBypass
 */
public class MainHook implements IXposedHookLoadPackage {

    private static final String[] TARGET_PACKAGES = {"com.UCMobile", "com.quark.browser"};

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) {
        if (!isTarget(lpparam.packageName)) return;

        ClassLoader cl = lpparam.classLoader;

        // 策略 A：Hook RewardedVideoAd 构造函数，接管 AdListener
        hookConstructor(lpparam);

        // 策略 B：拦截 canShow/isReady 状态查询，返回 false
        hookStateQuery(cl, lpparam);

        // 策略 C：拦截 show/play 展示方法，空实现 + 直触奖励
        hookShowMethod(cl, lpparam);

        // 策略 D：覆盖生命周期回调 onVideoComplete/onError
        hookLifecycle(cl, lpparam);
    }

    /** ========================================
     *  工具方法
     * ======================================== */

    private boolean isTarget(String pkg) {
        for (String t : TARGET_PACKAGES) {
            if (t.equals(pkg)) return true;
        }
        return false;
    }

    private void log(String msg) {
        XposedBridge.log("[BarbaBypass] " + msg);
    }

    /**
     * 安全查找类是否存在
     */
    private Class<?> safeFindClass(String name, ClassLoader cl) {
        try {
            return XposedHelpers.findClass(name, cl);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * 统一触发奖励的核心逻辑
     */
    private void fireReward(Object thisObj, LoadPackageParam lpparam) {
        if (thisObj == null) return;

        try {
            XposedHelpers.callMethod(thisObj, "onRewarded", thisObj);
            log("✓ 奖励已触发");
        } catch (Throwable t) {
            log("✗ 触发奖励失败: " + t.getMessage());
        }
    }

    /** ========================================
     *  策略 A：构造器 Hook -> 接管监听器
     * ======================================== */

    private void hookConstructor(LoadPackageParam lpparam) {
        Class<?> adClazz = safeFindClass("com.noah.api.RewardedVideoAd", lpparam.classLoader);
        if (adClazz == null) return;

        try {
            // 使用 Xposed API 82 标准签名：findAndHookConstructor(Class, Class[], hook)
            // 无参构造，传入空参数类型数组
            XposedHelpers.findAndHookConstructor(adClazz, new Class<?>[0],
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            // AdListener 作为构造函数参数，位于 param.args[0]
                            Object listener = param.args[0];
                            if (listener != null) {
                                hookAdListener(listener.getClass().getName(), lpparam);
                            }
                        }
                    });
            log("✓ 策略A: 已 Hook " + adClazz.getSimpleName() + " 构造器");
        } catch (Throwable t) {
            log("✗ 策略A 构造器 Hook 失败: " + t.getMessage());
        }
    }

    /**
     * 从构造函数参数中查找 AdListener 对象
     */
    private Object findListener(XC_MethodHook.MethodHookParam param) {
        if (param.args == null) return null;
        for (Object arg : param.args) {
            if (arg != null && isListenerLike(arg.getClass())) {
                return arg;
            }
        }
        return null;
    }

    private boolean isListenerLike(Class<?> clazz) {
        String name = clazz.getSimpleName().toLowerCase();
        if (name.contains("listener") || name.contains("callback") || name.contains("adlistener")) {
            return true;
        }
        return clazz.getSuperclass() == null;
    }

    private void hookAdListener(String listenerClass, LoadPackageParam lpparam) {
        try {
            XposedHelpers.findAndHookMethod(listenerClass,
                    lpparam.classLoader,
                    "onVideoStart",
                    "com.noah.api.RewardedVideoAd",
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            fireReward(param.thisObject, lpparam);
                        }
                    });
        } catch (Throwable t) {
            log("✗ 监听器 Hook 失败: " + t.getMessage());
        }
    }

    /** ========================================
     *  策略 B：状态查询拦截 -> canShow/isReady 返回 false
     * ======================================== */

    private void hookStateQuery(ClassLoader cl, LoadPackageParam lpparam) {
        Class<?> adClazz = safeFindClass("com.noah.api.RewardedVideoAd", cl);
        if (adClazz == null) return;

        String[] stateMethods = {"canShow", "isShow", "isReady", "canPlay"};
        int hooked = 0;
        for (String method : stateMethods) {
            try {
                XposedHelpers.findAndHookMethod(adClazz, method,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                log("⚡ 策略B: 拦截 " + method + "() -> false");
                                param.setResult(Boolean.FALSE);
                            }
                        });
                hooked++;
            } catch (Throwable ignored) {}
        }
        if (hooked > 0) {
            log("✓ 策略B: 状态查询 Hook 完成 (" + hooked + " 个方法)");
        }
    }

    /** ========================================
     *  策略 C：弹窗阻断 -> show() 空实现 + 立即触发奖励
     * ======================================== */

    private void hookShowMethod(ClassLoader cl, LoadPackageParam lpparam) {
        Class<?> adClazz = safeFindClass("com.noah.api.RewardedVideoAd", cl);
        if (adClazz == null) return;

        String[] showMethods = {"show", "play", "display", "showAd"};
        int hooked = 0;
        for (String method : showMethods) {
            try {
                XposedHelpers.findAndHookMethod(adClazz, method,
                        new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) {
                                log("⚡ 策略C: 拦截 " + method + "() -> 直接奖励");
                                fireReward(param.thisObject, lpparam);
                            }
                        });
                hooked++;
            } catch (Throwable ignored) {}
        }
        if (hooked > 0) {
            log("✓ 策略C: 弹窗阻断 Hook 完成 (" + hooked + " 个方法)");
        }
    }

    /** ========================================
     *  策略 D：生命周期接管 -> 各回调均触发奖励
     * ======================================== */

    private void hookLifecycle(ClassLoader cl, LoadPackageParam lpparam) {
        Class<?> adClazz = safeFindClass("com.noah.api.RewardedVideoAd", cl);
        if (adClazz == null) return;

        String[] lifeMethods = {"onVideoComplete", "onClose"};
        for (String method : lifeMethods) {
            try {
                XposedHelpers.findAndHookMethod(adClazz, method,
                        new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) {
                                log("⚡ 策略D: " + method + " -> 触发奖励");
                                fireReward(param.thisObject, lpparam);
                            }
                        });
            } catch (Throwable ignored) {}
        }

        // onError 有 int+String 参数
        try {
            XposedHelpers.findAndHookMethod(adClazz, "onError", int.class, String.class,
                    new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {
                            log("⚡ 策略D: onError -> 强制触发奖励");
                            fireReward(param.thisObject, lpparam);
                        }
                    });
        } catch (Throwable ignored) {}

        log("✓ 策略D: 生命周期 Hook 完成");
    }
}
