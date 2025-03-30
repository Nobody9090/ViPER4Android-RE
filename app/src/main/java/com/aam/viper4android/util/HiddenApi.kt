package com.aam.viper4android.util

import android.os.Build
import android.util.Property
import timber.log.Timber
import java.lang.reflect.Method

object HiddenApi {
    fun unseal() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return
        try {
            val methods = Property.of(Class::class.java, Array<Method>::class.java, "Methods")
                .get(Class.forName("dalvik.system.VMRuntime"))
            val setHiddenApiExemptions = methods.first { it.name == "setHiddenApiExemptions" }
            val getRuntime = methods.first { it.name == "getRuntime" }
            setHiddenApiExemptions.invoke(getRuntime.invoke(null), arrayOf("L"))
            Timber.d("unseal: success")
        } catch (e: Exception) {
            Timber.e(e, "unseal: failed")
        }
    }
}