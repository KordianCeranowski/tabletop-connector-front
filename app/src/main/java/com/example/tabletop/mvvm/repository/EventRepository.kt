package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.eventApi
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.util.Query
import retrofit2.Response

object EventRepository {

    suspend fun getMany(accessToken: String): Response<Many<Event>> {
        return eventApi.getMany(accessToken)
    }

    suspend fun getCustomMany(
        accessToken: String,
        options: Map<Query, String>
    ): Response<Many<Event>> {
        val result = options.map { it.key.value to it.value }.toMap()
        return eventApi.getCustomMany(accessToken, result)
    }

    suspend fun save(accessToken: String, eventRequest: EventRequest): Response<Event> {
        return eventApi.save(accessToken, eventRequest)
    }

    suspend fun getOne(accessToken: String, id: String): Response<Event> {
        return eventApi.getOne(accessToken, id)
    }

    suspend fun remove(accessToken: String, id: String): Response<Event> {
        return eventApi.remove(id)
    }

    suspend fun edit(accessToken: String, id: String, model: Event): Response<Event> {
        return eventApi.edit(id, model)
    }

    suspend fun participateInEvent(accessToken: String, id: String): Response<Unit> {
        return eventApi.participateInEvent(accessToken, id)
    }
}