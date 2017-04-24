Noseplug for Android
====================

Noseplug seeks to provide a convenient way for individuals in a community to
coordinate to control and mitigate noxious environmental odors.

---------------------------

Contents:
- `./android/`: Android app source code, using Android Studio with Gradle.
- `./functions/`: [Cloud Functions for Firebase][] source code.

Release downloads can be found on the [Releases page][].

[Cloud Functions for Firebase]: https://firebase.google.com/docs/functions/
[Releases page]: https://github.com/noseplug/monorepo/releases

# Getting started with Noseplug for Android (Installation Guide)

1. Download and unzip [noseplug-v1.zip][].
2. Copy `noseplug.apk` to your Android phone using a USB cable, a cloud service
   like Dropbox, or any other similar method for transferring files.
3. Open `noseplug.apk` and tap "**INSTALL**".

Noseplug should now be available in your launcher and immediately usable.

[noseplug-v1.zip]: https://github.com/noseplug/monorepo/files/952695/noseplug-v1.zip

# Development and Deployment

## Android

Clone this repository.
```
$  git clone https://github.com/noseplug/monorepo.git noseplug
```
Build the Android app with Gradle. This will take longer the first time it is run because it will automatically download
and install a gradle distribution, and download Noseplug's dependencies to a local cache.
```
$  cd noseplug/android
$  ./gradlew assembleDebug  # This will take longer the first time it is run.
$  cp app/build/outputs/apk/app-debug.apk noseplug.apk
```

After the APK is built, it can be installed by following the [getting started instructions][].
Alternatively, the project can be built with [Android Studio][] (which will run
gradle for you) or the APK can be installed using `./gradlew installDebug` with
a connected Android phone with USB debugging enabled.

[getting started instructions]: #getting-started-with-noseplug-for-android
[Android Studio]: https://developer.android.com/studio/index.html

## Functions

[Install Node.js and NPM.](https://docs.npmjs.com/getting-started/installing-node)

Install `firebase-tools`
```
npm install -g firebase-tools
```

Switch to the `functions` directory of your clone of this repository.
```
$ cd noseplug/functions
```

Install dependencies using NPM.
```
$ npm install
```

Login to `firebase-cli`. Follow the prompts and sign in to your Google Account
in your Web browser. Note that you have to be added as a contributor to the
project before you can log in: [noseplug-af01c][]

[noseplug-af01c]: https://console.firebase.google.com/project/noseplug-af01c/


```
$ firebase login
```

Deploy the cloud functions to Google Cloud.
```
$ firebase --only functions deploy
```

# Release Notes

## v1

New Features
* Real-time comments and events.
* Unattended user registration and login.
* Odor reports and data collection.
* Odor information page.
* New odor event notifications.

Bug Fixes
* Fixed crashing (before user's first sign-in) 162233f
* Fixed comments not loading on startup 779ed13

Known Bugs and Defects
* No unattended CSV data export.
* No historical / timeline data.
* No privileged interface for local officials.
* No hardening of realtime database.

**[Download Noseplug for Android v1](https://github.com/noseplug/monorepo/files/952695/noseplug-v1.zip)**

# Troubleshooting

### I can't install the app because of an "Unknown sources" warning.

You can temporarily disable this warning by taking the following steps:
1. Navigate to **Settings => Security => Device Administration** and enable "**Unknown
   sources**: Allow installation of apps from unknown sources". This may vary from
   device to device.
2. Retry the [getting started instructions][]
3. Navigate to **Settings => Security => Device Administration** and disable
   "**Unknown sources**".

### `./gradlew` installDebug fails.

If you are getting the following error...
```
FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:installDebug'.
> com.android.builder.testing.api.DeviceException: No connected devices!
```

...you have to enable Android debugging for ADB.

Navigate to **Settings => About phone**. Tap on **Build number** until a Toast
appears saying that Developer options is enabled (at least 7 times).

Navigate to **Settings => Developer options** and enable **Android debugging**.

### firebase-tools won't install.

You may need to run the command as `root` depending on your computer.

```
$ su
# npm install -g firebase-tools
# exit
$ firebase --help
```
