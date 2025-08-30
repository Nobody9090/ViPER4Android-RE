package com.aam.viper4android.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

object AndroidUtils {
    fun getApplicationLabel(context: Context, packageName: String) = try {
        val packageManager = context.packageManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0))
        } else {
            packageManager.getApplicationInfo(packageName, 0)
        }.loadLabel(packageManager).toString()
    } catch (e: Exception) {
        packageName
    }
}