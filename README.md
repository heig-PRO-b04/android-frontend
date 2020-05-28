# heig-PRO-b04/android-frontend

[![Build Status](https://travis-ci.com/heig-PRO-b04/android-frontend.svg?branch=master)](https://travis-ci.com/heig-PRO-b04/android-frontend)

An application to create, edit and delete polls that can be answered in real
time. In this repository, you'll find the codebase of the mobile application
of the system.

This software is developed as semester project (PRO) at HEIG-VD, academic year
2019/20.

## Development team:

| Name                                   |                                  |
|----------------------------------------|----------------------------------|
| Matthieu Burguburu (ass. project lead) | matthieu.burguburu@heig-vd.ch    |
| David Dupraz                           | david.dupraz@heig-vd.ch          |
| Clarisse Fleurimont                    | clarisse.fleurimont@heig-vd.ch   |
| Alexandre Piveteau (project lead)      | alexandre.piveteau@heig-vd.ch    |
| Guy-Laurent Subri                      | guy-laurent.subri@heig-vd.ch     |

## Dependencies

This project uses Gradle and the Android SDK will be needed.

## Installing

You can install the app via the [Google Play
store](https://play.google.com/store/apps/details?id=ch.heigvd.pro.b04.android), which we highly
encourage you to do. Alternatively, you can build it yourself.

### Building the release version

The release version can only be built by specific users who are allowed to sign the app. If you
need this access, please contact Alexandre and/or Guy-Laurent.

### Building the development version

The development version of the app will not include Firebase Crashlytics, but instead will output
debug information.

To build the app locally, you need to launch `./gradlew clean assembleDebug` to build two `.apk`
file. Those `apk` will be located at :

1. `app/build/outputs/apk/developmentAPI/debug/app-developmentAPI-debug.apk`
2. `app/build/outputs/apk/productionAPI/debug/app-productionAPI-debug.apk`

The `app-productionAPI-debug.apk` version will be built so that the app can communicate with our
backend (i.e. rockin.app). The `app-developmentAPI-debug.apk` version will be built so that you
can run your own version of the backend locally.

If you wish to use the backend locally, you will need to have a `local.properties` file at the root
of the project directory. It should contain the address and the port of your backend instance.

For example:

```
## This file must *NOT* be checked into Version Control Systems,
# as it contains information specific to your local configuration.
#
# Location of the SDK. This is only used by Gradle.
# For customization when using a Version Control System, please read the
# header note.
sdk.dir=/opt/android-sdk-update-manager
apiUrl="http://172.22.22.53:8080"

# Necessary for building the release version
#releaseStoreFile=
#releaseStorePassword=1234
#releaseKeyPassword=1234
```

Finally, whichever version you chose, you will need to transfer it to your phone and install it
manually. Please note that you may need to change your security settings to allow installing
applications from unknown sources.
