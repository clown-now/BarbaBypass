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

## 2026-07-27 - Task: Fix Xposed stub compilation errors from CI run 37
### What was done
- Corrected `XSharedPreferences` to implement the Android `SharedPreferences` interface instead of referencing an unresolved unqualified type.
- Removed an invalid helper that instantiated the abstract Xposed callback parameter type and removed an unused import.
### Testing
- Reviewed the available tail of the 24-error output from GitHub Actions run 30226269947; the visible override failures are consistent with the unresolved `SharedPreferences` interface and its nested listener/editor types. The complete Actions log was unavailable (HTTP 403), so the next CI run remains the authoritative check for any additional errors.
- Checked the stub against the Android API 34 `SharedPreferences` and `SharedPreferences.Editor` method set, including `apply()`.
- Full local Gradle verification remains unavailable because the terminal environment does not expose `/root/barba-bypass`; push this patch and use the repository's `assembleDebug` GitHub Actions job as the executable verification.
### Notes
- `app/src/main/xposed/de/robv/android/xposed/XSharedPreferences.java`: imported the Android `SharedPreferences` interface so its listener and editor methods resolve correctly.
- `app/src/main/xposed/de/robv/android/xposed/callbacks/XCallback.java`: removed the invalid instantiation of abstract `Param`.
- `app/src/main/xposed/de/robv/android/xposed/XC_MethodHook.java`: removed an unused Android import.
- `progress.md`: appended CI diagnosis, verification scope, and rollback details.
- Rollback: run `git restore app/src/main/xposed/de/robv/android/xposed/XSharedPreferences.java app/src/main/xposed/de/robv/android/xposed/callbacks/XCallback.java app/src/main/xposed/de/robv/android/xposed/XC_MethodHook.java progress.md`; the remote rollback point is commit `7f101915b06d9204b7e3a6d9704e29210788798c`.