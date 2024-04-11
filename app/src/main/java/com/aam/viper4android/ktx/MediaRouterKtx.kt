package com.aam.viper4android.ktx

import android.media.MediaRouter

fun MediaRouter.getSelectedLiveAudioRoute() = getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO)
