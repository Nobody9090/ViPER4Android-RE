package com.aam.viper4android.driver

enum class Architecture(val value: UByte) {
    UNKNOWN(0u),
    ARM(1u),
    ARM64(2u),
    X86(3u),
    X86_64(4u);

    companion object {
        fun fromValue(value: UByte) = entries.find { it.value == value } ?: UNKNOWN
    }
}