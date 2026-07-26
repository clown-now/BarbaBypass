## 2026-07-27 - Task: Repair the Android build configuration
### What was done
- Replaced the missing local Xposed API JAR dependency with a checked-in Android library module that compiles the existing API stubs.
- Kept the Xposed API dependency compile-only so stub classes are not packaged into the application APK.
- Documented the required JDK, Android SDK, and debug build command.
### Testing
- `git diff --check` identified and the edit removed a trailing blank-line issue in `app/build.gradle`.
- Gradle 8.7 was downloaded from an accessible mirror and its ZIP integrity check passed.
- Full `assembleDebug` verification could not complete in this environment: the original Gradle distribution URL timed out, no Android SDK/AGP cache was installed, and the terminal mount later stopped exposing `/root/barba-bypass`. Run `./gradlew assembleDebug --no-daemon` in a JDK 17 environment with Android SDK Platform 34 to complete APK verification.
### Notes
- `app/build.gradle`: replaced the nonexistent JAR dependency with a compile-only project dependency.
- `build.gradle`: declared the Android library plugin used by the local API module.
- `settings.gradle`: included the local `xposed-api` module.
- `xposed-api/build.gradle`: configured the existing Xposed stubs as an Android library source set.
- `xposed-api/src/main/AndroidManifest.xml`: added the minimal library manifest required by AGP.
- `docs/build.md`: documented build prerequisites, command, output, and dependency behavior.
- `progress.md`: recorded this task, verification evidence, and rollback instructions.
- Rollback: run `git restore app/build.gradle build.gradle settings.gradle` and remove `xposed-api/`, `docs/build.md`, and this appended progress entry. A precise rollback point is the pre-change commit `787217c`.