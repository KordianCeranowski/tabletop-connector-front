@file:Suppress("MoveVariableDeclarationIntoWhen")

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
import com.example.tabletop.mvvm.model.helpers.Profile
import com.google.gson.*
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import org.json.JSONTokener
import retrofit2.Response
import java.io.Serializable

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
        getMockUser(),
        getRandomDate(),
        getMockAddress(),
        emptyList(),
        emptyList()
    )
}

fun getMockListOfEvents(): List<Event> {
    return List(10) { idx ->
        Event("Event ${idx + 1}",
            getMockUser(),
            getRandomDate(),
            getMockAddress(),
            emptyList(),
            List(10) {
                Game(
                    "Name ${it + 1}",
                    "URI",
                    "URI",
                    2,
                    (3..8).random(),
                    (15..90 step 5).random()
                )
            }
        )
    }
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
    val year = "2020"

    val hours = "12"
    val minutes = "00"

    return StringBuilder()
        .append(year)
        .append('-')
        .append(month.firstDigit)
        .append(month.secondDigit)
        .append('-')
        .append(day.firstDigit)
        .append(day.secondDigit)
        .append('T')
        .append(hours)
        .append(':')
        .append(minutes)
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
        "URI",
        2,
        8,
        30
    )
}

fun getMockUser(): User {
    return User(
        "email",
        "username",
        getMockProfile()
    )
}

fun getMockProfile(): Profile {
    return Profile(
        "firstName",
        "lastName",
        "image"
    )
}

val Any.className: String
    get() = this::class.simpleName as String

fun EditText.setErrorEmpty() {
    this.error = "Field cannot be empty"
}

fun EditText.setErrorInvalid(fieldName: String) {
    this.error = "Please enter a valid $fieldName"
}

fun EditText.disableError() {
    this.error = null
}

// Serializer
fun <T> Response<T>.getFullResponse(showBody: Boolean = true): String {
    val body = body()?.let { gson.toJson(body()) }
    val bodyString =  if (showBody) "|Body: $body" else ""
    return """
        |
        |Headers:
        |${headers()}
        |Status: ${message()}
        |Code: ${code()}
        $bodyString
        """.trimMargin()
}

fun <T> Response<T>.status(): String = "${this.message()}\n"

fun <T> Response<T>.getErrorBodyProperties(): Map<String, String> {
    val errorBodyString = this.errorBody()?.string()

    val responseBody = JSONTokener(errorBodyString).nextValue()

    return when (responseBody) {
        is JsonArray -> emptyMap<String, String>().also {
            logW(responseBody.toString())
            logW("Response body is JsonArray")
        }
        is JsonPrimitive -> emptyMap<String, String>().also {
            logW("Response body is JsonPrimitive")
        }
        is JsonObject -> {
            val json = gson.fromJson(errorBodyString, JsonObject::class.java)

            json?.let {
                val keys = json.keySet().map { it.toString() }
                val entries = keys.map { key -> json[key].toString().removeDoubleQuotes() }
                keys.zip(entries).map { it.first to it.second }.toMap()
            } ?: emptyMap()
        }
        else -> emptyMap<String, String>().also {
            logW("Response body is of other type <${responseBody.className}>")
        }
    }
}

fun <T> Response<T>.resolve(onSuccess: () -> Any, onFailure: () -> Any) {
    if (isSuccessful) {
        onSuccess()
    } else {
        onFailure()
    }
}
