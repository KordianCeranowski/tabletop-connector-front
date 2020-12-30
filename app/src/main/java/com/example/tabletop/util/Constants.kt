package com.example.tabletop.util

import android.util.Patterns
import java.util.regex.Pattern

// EXTRAS
const val EXTRA_PROFILE_ID = "PROFILE_ID"

const val EXTRA_EVENT = "EVENT"

// URL
const val BASE_URL = "http://10.0.2.2:8000/api/"

// USER API
const val USER_API_ENDPOINT = "users/"

const val USER_API_ENDPOINT_REGISTER = "auth/users/"

const val USER_API_ENDPOINT_LOGIN = "auth/token/login/"

const val USER_API_ENDPOINT_LOGOUT = "auth/token/logout/"

const val USER_API_ENDPOINT_PROFILE = "profiles/"

const val USER_API_ENDPOINT_MY_PROFILE = "profiles/me/"

const val USER_API_ENDPOINT_EVENT_PARTICIPATION = "participation/"

const val USER_API_ENDPOINT_CREATE_PROFILE = "profiles/"

// GAME API
const val GAME_API_ENDPOINT = "games/"

// EVENT API
const val EVENT_API_ENDPOINT = "events/"

// VALIDATION PATTERN
enum class ValidationPattern(val value: Pattern) {
    EMAIL(
        Patterns.EMAIL_ADDRESS
    ),
    NICKNAME(
        Pattern.compile(
            "^" +
                    "(?=.*[A-z])" +      // at least 1 letter
                    ".{2,}" +           // at least 2 characters
                    "$"
        )
    ),
    PASSWORD(
        Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +     // at least 1 digit
                    "(?=.*[a-z])" +      // at least 1 lower case letter
                    "(?=.*[A-Z])" +      // at least 1 upper case letter
                    "(?=.*[@#$%^&+=])" + // at least 1 special character
                    "(?=\\S+$)" +        // now white spaces
                    ".{8,}" +           // at least 8 characters
                    "$"
        )
    )
}

// OTHER
enum class Sort {
    ID,
}

enum class Order {
    ASC,
    DESC;
}
