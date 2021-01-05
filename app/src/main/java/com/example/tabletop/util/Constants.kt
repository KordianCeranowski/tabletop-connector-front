package com.example.tabletop.util

import android.util.Patterns
import java.util.regex.Pattern

// DEVELOPMENT ONLY
const val USER_TEST_LOGIN = "testo"

const val USER_TEST_PASSWORD = "qwqwqwqW1$"

// ERROR MESSAGES
const val ERROR_MESSAGE_FAILURE = "Something went wrong"

// URL
const val BASE_URL = "http://10.0.2.2:8000/api/"

// USER API
const val USER_API_ENDPOINT = "users/"

const val USER_API_ENDPOINT_REGISTER = "auth/users/"

const val USER_API_ENDPOINT_DELETE_MY_ACCOUNT = "auth/users/me/"

const val USER_API_ENDPOINT_SET_PASSWORD = "auth/users/set_password/"

const val USER_API_ENDPOINT_SET_USERNAME = "auth/users/set_username/"

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

const val EVENT_API_ENDPOINT_CUSTOM = "events/search/"

// EXTRA
enum class Extra(private val value: String) {
    PROFILE_ID("PROFILE_ID"),
    EVENT("EVENT"),
    EVENT_ID("EVENT_ID"),
    IS_MY_EVENTS("IS_MY_EVENTS"),
    QUERY_MAP("QUERY_MAP");

    operator fun invoke() = this.value
}

// QUERY
enum class Query(private val value: String) {
    PARTICIPANT("participant"),
    DISTANCE("distance"),
    NAME("search"),
    DATE_FROM("date_from"),
    DATE_TO("date_to"),
    GEO_X("geo_x"),
    GEO_Y ("geo_y");

    operator fun invoke() = this.value
}

// VALIDATION PATTERN
enum class ValidationPattern(private val value: Pattern) {
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
    );

    operator fun invoke() = this.value
}
