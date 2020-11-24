package com.example.tabletop.repository

sealed class Repository
object UserRepository : Repository()
object EventRepository : Repository()
object MockRepository : Repository()