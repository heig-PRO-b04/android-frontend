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

This project uses Maven and is automatically built.

## Building and installing

To launch the app locally, you need to :

0. Set the Android SDK up on your computer.
1. Use `./gradlew clean assembleDebug assembleRelease testDebug` to run the
   unit tests and build an `.apk` file.
2. Install the `.apk` file manually to your device.

**Note :** it is also possible to run the project using Android Studio, which is less tedious. Just open the repository root as an Android project and you'll be good to go !