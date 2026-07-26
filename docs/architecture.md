# BarbaBypass 架构文档

## 概述

BarbaBypass 是一个 Xposed 模块，通过多层 Hook 策略绕过 UC浏览器/夸克浏览器中芭芭农场的视频广告。

## 目录结构

```
barba-bypass/
├── app/
│   ├── src/main/
│   │   ├── java/com/barbabypass/skip/
│   │   │   └── MainHook.java        # 核心模块
│   │   ├── res/values/
│   │   │   └── arrays.xml           # Xposed Scope 配置
│   │   └── AndroidManifest.xml      # 模块声明
│   ├── build.gradle                 # 应用构建配置
│   └── proguard-rules.pro           # ProGuard 规则
├── gradle/wrapper/
├── build.gradle                     # 根构建配置
├── settings.gradle
├── .gitignore
├── README.md
├── LICENSE
└── docs/
    └── architecture.md              # 本文档
```

## Hook 策略详解

### 策略 A — 构造器接管
- **触发时机：** `RewardedVideoAd` 对象创建时
- **行为：** 从构造函数参数中识别 `AdListener` 实例，Hook 其 `onVideoStart` 方法
- **效果：** 当广告开始播放时，直接调用 `onRewarded`，无需等待视频结束
- **依赖：** 能成功反射到构造函数参数中的 Listener 对象

### 策略 B — 状态查询拦截
- **触发时机：** 广告 SDK 查询"是否有可展示广告"时
- **Hook 方法：** `canShow()`, `isShow()`, `isReady()`, `canPlay()`
- **行为：** 强制返回 `false`
- **效果：** 让上层逻辑认为没有广告可用，跳过弹窗流程

### 策略 C — 弹窗阻断
- **触发时机：** 代码调用 `show()`, `play()`, `display()`, `showAd()` 时
- **行为：** 空实现 + 立即调用 `fireReward()`
- **效果：** 不仅阻止真实广告弹出，还同时触发奖励

### 策略 D — 生命周期接管
- **触发时机：** `onVideoComplete`, `onClose`, `onError` 回调
- **行为：** 在每个回调末尾调用 `fireReward()`
- **效果：** 即使广告已经播放了一半才生效，也能补发奖励；广告错误时也不会卡死流程

## 容错设计

1. **安全类查找：** `safeFindClass()` — 广告类不存在时不崩溃
2. **渐进式 Hook：** 每个策略独立 try-catch，一个失败不影响其他
3. **多别名覆盖：** 每种策略尝试多个可能的类名和方法名
4. **统一奖励触发：** `fireReward()` 集中处理，避免代码重复

## 日志标识

| 符号 | 含义 |
|------|------|
| ✓ | 成功 Hook |
| ✗ | Hook 失败 |
| ⚡ | 拦截/触发执行 |
| ℹ | 信息提示（类不存在等） |
