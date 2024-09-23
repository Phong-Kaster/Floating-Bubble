// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    /* Dependency injection with Hilt  - https://developer.android.com/training/dependency-injection/hilt-android#setup*/
    id("com.google.dagger.hilt.android") version "2.52" apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}