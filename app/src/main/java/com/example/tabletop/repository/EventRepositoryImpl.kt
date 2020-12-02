package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.eventApi
import com.example.tabletop.model.Event
import retrofit2.Response

suspend fun EventRepository.getAll(): Response<List<Event>> {
    return eventApi.getAll()
}

suspend fun EventRepository.getMany(sort: String, order: String): Response<List<Event>> {
    return eventApi.getMany(sort, order)
}

suspend fun EventRepository.getMany(options: Map<String, String>): Response<List<Event>> {
    return eventApi.getMany(options)
}

suspend fun EventRepository.save(event: Event): Response<Event> {
    return eventApi.save(event)
}

suspend fun EventRepository.getOne(id: String): Response<Event> {
    return eventApi.getOne(id)
}

suspend fun EventRepository.remove(id: String): Response<Event> {
    return eventApi.remove(id)
}

suspend fun EventRepository.edit(id: String, event: Event): Response<Event> {
    return eventApi.edit(id, event)
}
