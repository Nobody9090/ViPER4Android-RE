package com.aam.viper4android.ktx

import java.nio.ByteBuffer

fun ByteBuffer.putUByte(value: UByte): ByteBuffer = put(value.toByte())
fun ByteBuffer.putUShort(value: UShort): ByteBuffer = putShort(value.toShort())
fun ByteBuffer.putUInt(value: UInt): ByteBuffer = putInt(value.toInt())
fun ByteBuffer.putFloatArray(value: FloatArray): ByteBuffer = apply { value.forEach { putFloat(it) } }