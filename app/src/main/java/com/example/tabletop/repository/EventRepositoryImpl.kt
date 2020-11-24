package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.eventApi
import com.example.tabletop.model.Event
import retrofit2.Response

suspend fun EventRepository.getCustomUsers(sort: String, order: String): Response<List<Event>> {
    return eventApi.getCustomEvents(sort, order)
}

suspend fun EventRepository.getCustomUsers(options: Map<String, String>): Response<List<Event>> {
    return eventApi.getCustomEvents(options)
}

suspend fun EventRepository.save(event: Event): Response<Event> {
    return eventApi.save(event)
}

suspend fun EventRepository.getEvent(id: String): Response<Event> {
    return eventApi.getEvent(id)
}

suspend fun EventRepository.remove(id: String): Response<Event> {
    return eventApi.remove(id)
}

suspend fun EventRepository.edit(id: String, event: Event): Response<Event> {
    return eventApi.edit(id, event)
}
