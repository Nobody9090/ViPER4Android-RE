package com.aam.viper4android.view

import kotlin.math.atan2
import kotlin.math.sqrt

class Complex(private val real: Double, private val im: Double) {

    fun rho() = sqrt(real * real + im * im)

    @Suppress("unused")
    fun theta() = atan2(im, real)

    private fun con() = Complex(real, -im)

    fun add(other: Complex) = Complex(real + other.real, im + other.im)

    fun mul(other: Complex) =
        Complex(real * other.real - im * other.im, real * other.im + im * other.real)

    @Suppress("unused")
    fun mul(a: Double) = Complex(real * a, im * a)

    fun div(other: Complex): Complex {
        val lengthSquared = other.real * other.real + other.im * other.im
        return mul(other.con().div(lengthSquared))
    }

    fun div(a: Double) = Complex(real / a, im / a)
}