plugins {
    id ("com.android.application")
    kotlin ("android")
    id ("com.google.gms.google-services")
    id ("dagger.hilt.android.plugin")
    id ("com.google.firebase.crashlytics")
    id ("com.google.devtools.ksp")
}

android {
//    signingConfigs {
//        release {
//            storePassword ("ChHorizon0."
//            keyPassword ("ChHorizon0."
//            keyAlias "key0"
//            storeFile file("D:\\ProjectFiles\\Kotlin\\EClass\\keystore\\ClassKonnect.jks")
//        }
//    }


    namespace = "com.femi.e_class"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.femi.e_class"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
            abiFilters.add("armeabi-v7a")
        }
    }

    buildTypes {
        release {
//            minifyEnabled true
//            shrinkResources = true
            isMinifyEnabled = false
            proguardFiles (
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
//            signingConfig signingConfigs.release
        }

        debug {
            isDebuggable = true
//            shrinkResources = true
//            minifyEnabled true
//            signingConfig signingConfigs.debug
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
//    buildToolsVersion ("33.0.0"
//    lint {
//        abortOnError false
//    }
}

dependencies {

    val composeVersion = "1.5.3"

    implementation ("com.google.firebase:firebase-firestore-ktx:24.8.1")
    implementation ("com.google.firebase:firebase-firestore:24.8.1")
    implementation ("com.google.firebase:firebase-auth:22.1.2")
    implementation(platform("com.google.firebase:firebase-bom:31.1.1"))
    implementation ("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation ("com.google.firebase:firebase-crashlytics-ktx:18.4.3")

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("com.google.truth:truth:1.0.1")

    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:$composeVersion")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("com.google.truth:truth:1.0.1")

    //Compose
    implementation ("androidx.compose.ui:ui:$composeVersion")
    implementation ("androidx.compose.material3:material3:1.2.0-alpha09")
    implementation ("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation ("androidx.navigation:navigation-compose:2.7.4")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    debugImplementation ("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:$composeVersion")

    //Splash Screen
    implementation ("androidx.core:core-splashscreen:1.0.1")

    //Preferences Datastore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    //OnBoarding Pager
    implementation ("com.google.accompanist:accompanist-pager:0.28.0")

    //OnBoarding Pager Indicator
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // Jitsi Meet
    implementation ("org.jitsi.react:jitsi-meet-sdk:8.4.0") {
        isTransitive = true
    }

    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.48")
    ksp ("com.google.dagger:hilt-android-compiler:2.48")
    ksp ("androidx.hilt:hilt-compiler:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.hilt:hilt-navigation:1.0.0")


}