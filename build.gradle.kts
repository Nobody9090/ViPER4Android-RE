// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply true
    alias(libs.plugins.kotlin.android) apply true
    alias(libs.plugins.kotlin.compose) apply true
    alias(libs.plugins.hilt.android) apply true
    alias(libs.plugins.ksp) apply true
}