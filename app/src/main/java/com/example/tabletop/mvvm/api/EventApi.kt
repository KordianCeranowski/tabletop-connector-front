package com.example.tabletop.mvvm.api

import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.Constants.EVENT_API_ENDPOINT
import retrofit2.Response
import retrofit2.http.*

interface EventApi {
    @GET(EVENT_API_ENDPOINT)
    suspend fun getMany(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<Many<Event>>

    @GET(EVENT_API_ENDPOINT)
    suspend fun getMany(
        @QueryMap options: Map<String, String>
    ): Response<Many<Event>>

    // todo access
    // @Headers("Authorization: ")
    @POST(EVENT_API_ENDPOINT)
    suspend fun save(
        @Body event: Event
    ): Response<Event>

    @GET("$EVENT_API_ENDPOINT{id}")
    suspend fun getOne(
        @Path("id") id: String
    ): Response<Event>

    @DELETE("$EVENT_API_ENDPOINT{id}")
    suspend fun remove(
        @Path("id") id: String
    ): Response<Event>

    @PUT("$EVENT_API_ENDPOINT{id}")
    suspend fun edit(
        @Path("id") id: String,
        @Body event: Event
    ): Response<Event>
}