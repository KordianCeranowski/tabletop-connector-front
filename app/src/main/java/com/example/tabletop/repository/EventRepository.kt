package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.eventApi
import com.example.tabletop.model.Event
import retrofit2.Response

class EventRepository : Repository() {
    suspend fun getCustomUsers(sort: String, order: String): Response<List<Event>> {
        return eventApi.getCustomEvents(sort, order)
    }

    suspend fun getCustomUsers(options: Map<String, String>): Response<List<Event>> {
        return eventApi.getCustomEvents(options)
    }

    suspend fun save(event: Event): Response<Event> {
        return eventApi.save(event)
    }

    suspend fun getEvent(id: String): Response<Event> {
        return eventApi.getEvent(id)
    }

    suspend fun remove(id: String): Response<Event> {
        return eventApi.remove(id)
    }

    suspend fun edit(id: String, event: Event): Response<Event> {
        return eventApi.edit(id, event)
    }
}