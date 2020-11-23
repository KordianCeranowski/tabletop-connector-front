package com.example.tabletop.api

import com.example.tabletop.model.Event
import com.example.tabletop.model.User
import retrofit2.Response
import retrofit2.http.*

interface EventApi {
    @GET("event")
    suspend fun getCustomEvents(
        @Query("_sort") sort: String,
        @Query("_order") order: String
    ): Response<List<Event>>

    @GET("event")
    suspend fun getCustomEvents(
        @QueryMap options: Map<String, String>
    ): Response<List<Event>>

    @POST("event")
    suspend fun save(
        @Body event: Event
    ): Response<Event>

    @GET("event/{id}")
    suspend fun getEvent(
        @Path("id") id: Int
    ): Response<Event>

    @DELETE("event/{id}")
    suspend fun remove(
        @Path("id") id: Int
    ): Response<Event>

    @PUT("event/{id}")
    suspend fun edit(
        @Path("id") id: Int,
        @Body event: Event
    ): Response<Event>
}