// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
    id("androidx.room") version "2.6.1" apply false
}