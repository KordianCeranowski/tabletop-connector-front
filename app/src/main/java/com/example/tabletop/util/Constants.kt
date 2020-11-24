package com.example.tabletop.util

import android.util.Patterns
import java.util.regex.Pattern

object Constants {
    const val MOCK_BASE_URL = "https://jsonplaceholder.typicode.com"

    const val BASE_URL = "localhost:8000/api"

    // VALIDATION PATTERNS
    val EMAIL_PATTERN: Pattern = Patterns.EMAIL_ADDRESS

    val NICKNAME_PATTERN: Pattern =
        Pattern.compile(
            "^" +
                    "(?=.*[A-z])"+      // at least 1 letter
                    ".{2,}" +           // at least 2 characters
                    "$")

    val PASSWORD_PATTERN: Pattern =
        Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +     // at least 1 digit
                    "(?=.*[a-z])"+      // at least 1 lower case letter
                    "(?=.*[A-Z])"+      // at least 1 upper case letter
                    "(?=.*[@#$%^&+=])"+ // at least 1 special character
                    "(?=\\S+$)"+        // now white spaces
                    ".{8,}" +           // at least 8 characters
                    "$")

    // USER API
    const val USER_API_ENDPOINT = "users"

    const val USER_API_REGISTER_ENDPOINT = "auth/jwt/create"

    const val USER_API_LOGIN_ENDPOINT = "BLANK"//todo

    // EVENT API
    const val EVENT_API_ENDPOINT = "events"

    enum class LogType {
        INFO, ERROR, WARNING, SUCCESS
    }

    enum class Sort {
        ID,
    }

    enum class Order {
        ASC,
        DESC;
    }
}