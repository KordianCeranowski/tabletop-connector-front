package com.example.tabletop.repository

import com.example.tabletop.api.RetrofitInstance.eventApi
import com.example.tabletop.model.Event
import retrofit2.Response

object EventRepository : Repository<Event>() {
    override suspend fun getMany(sort: String, order: String): Response<List<Event>> {
        return eventApi.getMany(sort, order)
    }

    override suspend fun getMany(options: Map<String, String>): Response<List<Event>> {
        return eventApi.getMany(options)
    }

    override suspend fun save(model: Event): Response<Event> {
        return eventApi.save(model)
    }

    override suspend fun getOne(id: String): Response<Event> {
        return eventApi.getOne(id)
    }

    override suspend fun remove(id: String): Response<Event> {
        return eventApi.remove(id)
    }

    override suspend fun edit(id: String, model: Event): Response<Event> {
        return eventApi.edit(id, model)
    }
}