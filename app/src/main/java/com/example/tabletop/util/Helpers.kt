 package com.example.tabletop.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.example.tabletop.model.Event
import com.example.tabletop.model.Game
import com.example.tabletop.model.User
import com.example.tabletop.model.helpers.Address
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.alexandroid.utils.mylogkt.logD
import retrofit2.Response
import java.io.Serializable
import java.util.*
import kotlin.concurrent.schedule

object Helpers {

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    // Unsafe
    inline fun <reified T : Activity> ComponentActivity.startWithExtra(
        vararg pairs: Pair<String, Any>
    ) {
        Intent(this, T::class.java).also { intent ->
            pairs.forEach {
                val pairValue = when (it.second) {
                    is Boolean -> it.second as Boolean
                    is Char -> it.second as Char
                    is String -> it.second as String
                    is Byte -> it.second as Byte
                    is Short -> it.second as Short
                    is Int -> it.second as Int
                    is Long -> it.second as Long
                    is Float -> it.second as Float
                    is Double -> it.second as Double
                    is Serializable -> it.second as Serializable
                    else -> throw Exception("Invalid value type")
                }
                intent.putExtra(it.first, pairValue)
            }
            startActivity(intent)
        }
    }

    inline fun <reified T : Activity> Context.startWithExtra(vararg pairs: Pair<String, Any>) {
        Intent(this, T::class.java).also { intent ->
            pairs.forEach {
                val pairValue = when (it.second) {
                    is Boolean -> it.second as Boolean
                    is Char -> it.second as Char
                    is String -> it.second as String
                    is Byte -> it.second as Byte
                    is Short -> it.second as Short
                    is Int -> it.second as Int
                    is Long -> it.second as Long
                    is Float -> it.second as Float
                    is Double -> it.second as Double
                    is Serializable -> it.second as Serializable
                    else -> throw Exception("Invalid value type")
                }
                intent.putExtra(it.first, pairValue)
            }
            startActivity(intent)
        }
    }

    fun getMockEvent(): Event {
        return Event(
            "mock name",
            "mock creator",
            getRandomDate(),
            getMockAddress(),
            emptyList(),
            emptyList()
        )
    }

    fun getRandomDate() : String {
        val day = object {
            val firstDigit = (0..3).random()
            val secondDigit = when (firstDigit) {
                0 -> (1..9)
                3 -> (0..1)
                else -> (0..9)
            }.random()
        }
        val month = object {
            val firstDigit = (0..1).random()
            val secondDigit = (if (firstDigit == 0) (1..9) else (0..2)).random()
        }
        return StringBuilder()
            .append(day.firstDigit)
            .append(day.secondDigit)
            .append(".")
            .append(month.firstDigit)
            .append(month.secondDigit)
            .toString()
    }

    fun getMockAddress(): Address {
        return Address(
            "BLANK",
            "BLANK",
            "BLANK",
            "BLANK",
            "BLANK",
            0.0,
            0.0
        )
    }

    fun getMockGame(): Game {
        return Game(
            "name",
            "URL",
            2,
            8,
            30
        )
    }

    fun getMockUser(): User {
        return User(
            "email",
            "username",
            "password",
        )
    }

    fun getEditTextString(vararg editTexts: EditText): List<String> {
        return editTexts.map { it.text.toString().trim() }
    }

    val Any.className: String
        get() = this::class.simpleName as String

    fun <T> Response<T>.getFullResponse(): String {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        val body = if (body() == null) errorBody() else body()// elvis
        return """
            |
            |Headers:
            |${headers()}
            |Status: ${message()}
            |Code: ${code()}
            |Body: ${gson.toJson(body)}
            """.trimMargin()
    }

    //todo
    //MY_LOG_KT EXTENSION FUNCTIONS
    fun logIt(vararg msgs: Any?) {
        msgs.forEach { msg ->
            Timer().schedule(1) {
                msg?.let { logD(it.toString()) } ?: logD("[ERROR]: Object is null")
            }
        }
    }

    /*
    todo to use in Activity
    saveEventAndExecute(eventViewModel::save, newEvent) {
            args -> args.forEach { logD(it.toString()) }
    }
    */

    /*
    private fun <T> saveEventAndExecute(
        request: KFunction<Unit>,
        event: T,
        args: List<Any> = emptyList(),
        action: (List<Any>) -> Unit
    ) {
        //request.invoke

        eventViewModel.responseOne.observe(this, { response ->
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    action(listOf(it))
                }
            } else {
                logIt(response.errorBody())
                // testTextView.text = response.code().toString()
            }
        })
    }
    */
}