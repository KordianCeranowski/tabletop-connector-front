 package com.example.tabletop.util

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tabletop.model.Address
import com.example.tabletop.model.Event
import com.example.tabletop.model.User
import com.example.tabletop.repository.*
import com.example.tabletop.viewmodel.ViewModelFactory
import com.example.tabletop.viewmodel.MockViewModel
import com.example.tabletop.viewmodel.UserViewModel
import com.example.tabletop.viewmodel.EventViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.alexandroid.utils.mylogkt.logD
import retrofit2.Response
import java.io.Serializable
import java.util.*
import kotlin.concurrent.schedule

object Helpers {

    val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    inline fun <reified T : Activity> AppCompatActivity.startWithExtra(
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

    fun AppCompatActivity.getMockEvent(): Event {
        return Event(
            "name",
            "creator",
            "date",
            Address(
                "country",
                "city",
                "street",
                "postal_code",
                "number",
                1.0,
                1.0
            ),
            null)
    }

    fun AppCompatActivity.getMockUser(): User {
        return User(
            "email",
            "username",
            "password",
        )
    }

    fun AppCompatActivity.viewModelOf(repository: Repository): ViewModel {
        val viewModel = when (repository) {
            is UserRepository -> UserViewModel(repository)
            is EventRepository -> EventViewModel(repository)
            is MockRepository -> MockViewModel(repository)
        }
        return ViewModelProvider(this, ViewModelFactory(repository))
            .get(viewModel::class.java)
    }

    fun AppCompatActivity.getEditTextString(vararg editTexts: EditText): List<String> {
        return editTexts.map { it.text.toString().trim() }
    }

    fun Any.getClassName(): String = this::class.simpleName as String

    fun <T> Response<T>.getFullResponse(): String {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        val body = if (body() == null) errorBody() else body()
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
    // MY_LOG_KT EXTENSION FUNCTIONS
    fun AppCompatActivity.logIt(vararg msgs: Any?) {
        msgs.forEach { msg ->
            Timer().schedule(1) {
                logD(msg.toString())
            }
        }
    }

    // fun AppCompatActivity.logError(vararg msgs: Any?, tag: String = this.getClassName()) {
    //     val logTypeString = "[ERROR] "
    //     msgs.forEach { msg ->
    //         Timer().schedule(1) {
    //             Log.d("[$tag]", "$logTypeString${msg.toString()}")
    //         }
    //     }
    // }

    //todo to use in Activity
    // saveEventAndExecute(eventViewModel::save, newEvent) {
    //         args -> args.forEach { logD(it.toString()) }
    // }

    // private fun <T> saveEventAndExecute(
    //     request: KFunction<Unit>,
    //     event: T,
    //     args: List<Any> = emptyList(),
    //     action: (List<Any>) -> Unit
    // ) {
    //     //request.invoke
    //
    //     eventViewModel.responseOne.observe(this, { response ->
    //         if (response.isSuccessful) {
    //             val body = response.body()
    //             body?.let {
    //                 action(listOf(it))
    //             }
    //         } else {
    //             logIt(response.errorBody())
    //             // testTextView.text = response.code().toString()
    //         }
    //     })
    // }
}
