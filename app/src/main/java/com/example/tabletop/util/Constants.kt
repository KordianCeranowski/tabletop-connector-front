package com.example.tabletop.util

object Constants {
    const val MOCK_BASE_URL = "https://jsonplaceholder.typicode.com"

    const val BASE_URL = "localhost:8000/api"

    //USER API
    const val USER_API_ENDPOINT = "users"

    const val USER_API_REGISTER_ENDPOINT = "auth/jwt/create"

    const val USER_API_LOGIN_ENDPOINT = "BLANK"//todo

    //EVENT API
    const val EVENT_API_ENDPOINT = "events"


    enum class LogType {
        INFO, ERROR, WARNING, SUCCESS
    }

    enum class Sort {
        ID,
        DESC;
    }

    enum class Order {
        ASC,
        DESC;
    }
}