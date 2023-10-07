plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.0.0" apply false
    kotlin("android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}

buildscript {

    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath("com.android.tools.build:gradle:8.0.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}// Top-level build file where you can add configuration options common to all sub-projects/modules.
