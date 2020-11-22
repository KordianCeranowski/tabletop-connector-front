package com.example.tabletop.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.internal.Intrinsics
import kotlin.reflect.KClass

class Helpers {
    companion object {
        fun <A : AppCompatActivity, B : Any> A.justStartActivity(kClass: KClass<B>) {
            startActivity(Intent(this, kClass.java))
        }

        fun Any.getClassName(): String = this::class.simpleName as String
    }
}
