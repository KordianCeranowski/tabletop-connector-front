package com.example.tabletop.util

import net.alexandroid.utils.mylogkt.MyLogKt

fun runLoggingConfig() {
    MyLogKt.apply {
        tag = "Log"
        isSpacingEnabled = true
        isMethodNameVisible = true
        isLengthShouldWrap = false
    }
}

