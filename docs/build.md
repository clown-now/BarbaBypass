# Build

## Requirements

- JDK 17
- Android SDK Platform 34
- Gradle Wrapper (included)

## Debug APK

```sh
./gradlew assembleDebug --no-daemon
```

The APK is written to `app/build/outputs/apk/debug/`.

The `xposed-api` module compiles the checked-in Xposed API stubs. The app uses it as a `compileOnly` dependency, so the stub classes are not packaged into the APK.
