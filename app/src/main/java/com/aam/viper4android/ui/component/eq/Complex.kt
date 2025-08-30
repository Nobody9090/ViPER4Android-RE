package com.aam.viper4android.ui.component.eq

import kotlin.math.sqrt

class Complex(
    private val real: Double,
    private val imaginary: Double
) {
    operator fun plus(other: Complex): Complex {
        return Complex(
            real + other.real,
            imaginary + other.imaginary
        )
    }

    operator fun times(other: Complex): Complex {
        return Complex(
            real * other.real - imaginary * other.imaginary,
            real * other.imaginary + imaginary * other.real
        )
    }

    operator fun div(other: Complex): Complex {
        val denominator = other.real * other.real + other.imaginary * other.imaginary
        return Complex(
            (real * other.real + imaginary * other.imaginary) / denominator,
            (imaginary * other.real - real * other.imaginary) / denominator
        )
    }

    fun abs(): Double {
        return sqrt(real * real + imaginary * imaginary)
    }
}