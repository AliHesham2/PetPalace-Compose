// Top-level build file where you can add configuration options common to all subprojects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false//parcelize
    alias (libs.plugins.kotlin.hilt) apply false //hilt
    alias (libs.plugins.kotlin.ksp) apply false //ksp
}