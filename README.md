Noseplug for Android
====================

Noseplug seeks to provide a convenient way for individuals in a community to
coordinate to control and mitigate noxious environmental odors.

---------------------------

Contents:
- `./android/`: Android app source code, using Android Studio with Gradle.
- `./functions/`: [Cloud Functions for Firebase][] source code.

Releases can be found on the [Releases page][].

[Cloud Functions for Firebase]: https://firebase.google.com/docs/functions/
[Releases page]: https://github.com/noseplug/monorepo/releases

# Getting Started with Noseplug for Android

1. Download and unzip [noseplug-v1.zip][].
2. Copy `noseplug.apk` to your Android phone using a USB cable, a cloud service
   like Dropbox, or any other similar method for transferring files.
3. Navigate to **Settings => Security => Device Administration** and enable "**Unknown
   sources**: Allow installation of apps from unknown sources". This may vary from
   device to device.
4. Open `noseplug.apk` and tap "**INSTALL**".
5. Navigate to **Settings => Security => Device Administration** and disable
   "**Unknown sources**".

Noseplug should now be available in your launcher.

[noseplug-v1.zip]: https://github.com/noseplug/monorepo/files/952695/noseplug-v1.zip

# Development and Deployment
## Android
Clone this repository
```
 $  git clone https://github.com/noseplug/monorepo.git noseplug
```
Build the Android app with Gradle
```
 $  cd noseplug/android
 $  ./gradlew assembleDebug  # This will take longer the first time it is run.
 $  cp app/build/outputs/apk/app-debug.apk noseplug.apk
```

After the APK is built, it can be installed by following the [getting started instructions][].
Alternatively, the project can be built with Android Studio (which will run
gradle for you) or the APK can be installed using `./gradlew installDebug` with
a connected Android phone with USB debugging enabled.

[getting started instructions]: #getting-started-with-noseplug-for-android

## Functions
