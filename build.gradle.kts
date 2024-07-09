// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//
//    repositories {
//        google()
//        mavenCentral()
//    }
//
//    dependencies {
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
//    }
//}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.daggerHiltAndroid) apply false
    alias(libs.plugins.androidx.navigation.safeargs.kotlin) apply false
}