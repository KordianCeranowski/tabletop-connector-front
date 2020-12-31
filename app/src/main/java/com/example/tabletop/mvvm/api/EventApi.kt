package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.util.EVENT_API_ENDPOINT
import com.example.tabletop.util.USER_API_ENDPOINT_EVENT_PARTICIPATION
import retrofit2.Response
import retrofit2.http.*

interface EventApi {

    @GET(EVENT_API_ENDPOINT)
    suspend fun getMany(
        @Header("Authorization") auth: String,
        @QueryMap options: Map<String, String>
    ): Response<Many<Event>>

    @GET("$EVENT_API_ENDPOINT{id}/")
    suspend fun getOne(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String
    ): Response<Event>

    @POST(EVENT_API_ENDPOINT)
    suspend fun save(
        @Header("Authorization") auth: String,
        @Body eventRequest: EventRequest
    ): Response<Event>

    @DELETE("$EVENT_API_ENDPOINT{id}/")
    suspend fun remove(
        @Path("id") id: String
    ): Response<Event>

    @PATCH("$EVENT_API_ENDPOINT{id}/")
    suspend fun edit(
        @Path("id") id: String,
        @Body event: Event
    ): Response<Event>

    @PATCH("$USER_API_ENDPOINT_EVENT_PARTICIPATION{id}/")
    suspend fun participateInEvent(
        @Header("Authorization") auth: String,
        @Path("id") id: String
    ): Response<Unit>
}