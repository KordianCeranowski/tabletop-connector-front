package com.example.tabletop.mvvm.repository

import com.example.tabletop.mvvm.api.RetrofitInstance.eventApi
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import retrofit2.Response

object EventRepository {

    suspend fun getMany(
        accessToken: String,
        options: Map<String, String>
    ): Response<Many<Event>> {
        return eventApi.getMany(accessToken, options)
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

    suspend fun joinOrLeaveEvent(accessToken: String, id: String): Response<Unit> {
        return eventApi.joinOrLeaveEvent(accessToken, id)
    }
}