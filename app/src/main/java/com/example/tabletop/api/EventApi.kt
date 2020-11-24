package com.example.tabletop.api

import com.example.tabletop.model.Event
import com.example.tabletop.util.Constants.EVENT_API_ENDPOINT
import retrofit2.Response
import retrofit2.http.*

interface EventApi {
    @GET(EVENT_API_ENDPOINT)
    suspend fun getCustomEvents(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<List<Event>>

    @GET(EVENT_API_ENDPOINT)
    suspend fun getCustomEvents(
        @QueryMap options: Map<String, String>
    ): Response<List<Event>>

    @POST(EVENT_API_ENDPOINT)
    suspend fun save(
        @Body event: Event
    ): Response<Event>

    @GET("$EVENT_API_ENDPOINT/{id}")
    suspend fun getEvent(
        @Path("id") id: String
    ): Response<Event>

    @DELETE("$EVENT_API_ENDPOINT/{id}")
    suspend fun remove(
        @Path("id") id: String
    ): Response<Event>

    @PUT("$EVENT_API_ENDPOINT/{id}")
    suspend fun edit(
        @Path("id") id: String,
        @Body event: Event
    ): Response<Event>
}