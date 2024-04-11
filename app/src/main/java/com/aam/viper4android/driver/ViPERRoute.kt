package com.aam.viper4android.driver

import android.media.MediaRouter

interface ViPERRoute {
    fun getId(): String
    fun getName(): CharSequence

    companion object {
        fun fromRouteInfo(route: MediaRouter.RouteInfo): ViPERRoute {
            return object : ViPERRoute {
                override fun getId(): String {
                    return route.name.toString()
                }

                override fun getName(): CharSequence {
                    return route.name
                }
            }
        }
    }
}