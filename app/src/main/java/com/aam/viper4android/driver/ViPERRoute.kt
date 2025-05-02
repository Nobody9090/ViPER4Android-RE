package com.aam.viper4android.driver

import androidx.mediarouter.media.MediaRouter

interface ViPERRoute {
    fun getId(): String
    fun getName(): CharSequence

    companion object {
        fun fromRouteInfo(route: MediaRouter.RouteInfo): ViPERRoute {
            return object : ViPERRoute {
                override fun getId(): String {
                    return route.id
                }

                override fun getName(): CharSequence {
                    val name = route.name

                    // Weird HyperOS behavior
                    val annoyingText = "dontapplycevolume"
                    return if (name.startsWith(annoyingText)) {
                        name.substring(annoyingText.length)
                    } else {
                        name
                    }
                }
            }
        }
    }
}