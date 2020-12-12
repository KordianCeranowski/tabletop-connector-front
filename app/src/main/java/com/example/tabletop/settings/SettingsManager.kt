package com.example.tabletop.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsManager(context: Context) {
    private val dataStore = context.createDataStore(name = "settings")

    companion object {
        val IS_USER_LOGGED_IN = preferencesKey<Boolean>("isUserLoggedIn")
        val USER_LONGITUDE = preferencesKey<Int>("userLongitude")
        val USER_LATITUDE = preferencesKey<Int>("userLatitude")
    }

    suspend fun setIsUserLoggedIn(isUserLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_USER_LOGGED_IN] = isUserLoggedIn
        }
    }

    suspend fun setUserLongitude(userLongitude: Int) {
        dataStore.edit { preferences ->
            preferences[USER_LONGITUDE] = userLongitude
        }
    }

    suspend fun setUserLatitude(userLatitude: Int) {
        dataStore.edit { preferences ->
            preferences[USER_LATITUDE] = userLatitude
        }
    }

    val isUserLoggedInFlow: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference -> preference[IS_USER_LOGGED_IN] ?: false }

    val userLongitudeFlow: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference -> preference[USER_LONGITUDE] ?: 0 }

    val userLatitudeFlow: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preference -> preference[USER_LATITUDE] ?: 0 }
}