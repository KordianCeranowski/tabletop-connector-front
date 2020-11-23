package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance
import com.example.tabletop.model.Event
import com.example.tabletop.model.User
import retrofit2.Response

class EventRepository : Repository() {
    suspend fun getCustomUsers(sort: String, order: String): Response<List<Event>> {
        return RetrofitInstance.eventApi.getCustomEvents(sort, order)
    }

    suspend fun getCustomUsers(options: Map<String, String>): Response<List<Event>> {
        return RetrofitInstance.eventApi.getCustomEvents(options)
    }

    suspend fun save(event: Event): Response<Event> {
        return RetrofitInstance.eventApi.save(event)
    }

    suspend fun getEvent(id: Int): Response<Event> = RetrofitInstance.eventApi.getEvent(id)

    suspend fun remove(id: Int): Response<Event> = RetrofitInstance.eventApi.remove(id)

    suspend fun edit(id: Int, event: Event): Response<Event> = RetrofitInstance.eventApi.edit(id, event)
}