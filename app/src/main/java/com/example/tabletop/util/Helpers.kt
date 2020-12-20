package com.example.tabletop.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.mvvm.model.helpers.Address
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import net.alexandroid.utils.mylogkt.logD
import retrofit2.Response
import java.io.Serializable

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

    // Serializer
    fun <T> Response<T>.getFullResponse(): String {
        val body = body()?.let { gson.toJson(body()) }
        return """
            |
            |Headers:
            |${headers()}
            |Status: ${message()}
            |Code: ${code()}
            |Body: $body
            """.trimMargin()
    }

    fun <T> Response<T>.getErrorBodyProperties(): Map<String, String> {
        val json = gson.fromJson(this.errorBody()?.string(), JsonObject::class.java)
        val keys = json.keySet().map { it.toString() }
        val entries = keys.map { key -> json[key].toString().removeDoubleQuotes() }
        return keys.zip(entries).map { it.first to it.second }.toMap()
    }
}