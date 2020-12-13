package com.example.tabletop.util

import net.alexandroid.utils.mylogkt.MyLogKt
import net.alexandroid.utils.mylogkt.logI

fun runLoggingConfig() {
    MyLogKt.apply {
        logI("Starting logging configuration")
        tag = "Log"
        isSpacingEnabled = true
        isMethodNameVisible = true
        isLengthShouldWrap = false
        isTimeVisible = false
    }
}

