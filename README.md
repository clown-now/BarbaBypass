# BarbaBypass

> 跳过 UC浏览器 / 夸克浏览器 芭芭农场广告，直接获取奖励

## 功能

本 Xposed 模块通过多层 Hook 策略绕过芭芭农场的视频广告，使用户无需真实观看即可获得水滴/积分奖励。

### 策略总览

| 策略 | 作用 | 覆盖 |
|------|------|------|
| **状态拦截** | 让广告 SDK 认为"无可展示广告" | `canShow()`, `isReady()` |
| **弹窗阻断** | 阻止真实广告视频弹出 | `show()`, `play()` |
| **生命周期接管** | 在任意回调中触发奖励 | `onVideoStart`, `onVideoComplete`, `onError` |
| **直接奖励** | 不调用原方法，立即发放奖励 | `show()` 内联触发 |
| **监听器 Hook** | 原始策略：接管 AdListener | `RewardedVideoAd` 构造器 |

## 支持应用

- UC浏览器 (`com.UCMobile`)
- 夸克浏览器 (`com.quark.browser`)

## 要求

- Android 5.0+ (API 21+)
- Root 权限
- Xposed Framework 或 LSPosed

## 安装

1. 解锁并 Root 你的设备
2. 安装 [LSPosed](https://github.com/LSPosed/LSPosed)（推荐 Magisk 版）
3. 安装 BarbaBypass APK
4. 在 LSPosed 管理器中启用 BarbaBypass
5. 将 UC浏览器/夸克浏览器添加到模块的 Scope 列表
6. 重启设备

## 许可证

[MIT License](LICENSE)

## 免责声明

本工具仅供学习与技术研究用途。使用本模块可能导致账号被平台风控系统标记，使用者需自行承担风险。

## 技术文档

详见 [docs/](docs/) 目录
