package com.example.tabletop.util

    operator fun String.times(n: Int): String {
        var result = ""
        (0 until n).forEach { _ -> result += this }
        return result
    }
