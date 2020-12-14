package com.example.tabletop.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
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

    val userId: Flow<String> = getFlow { it[USER_ID] ?: ""  }

    val isUserLoggedInFlow: Flow<Boolean> = getFlow { it[IS_USER_LOGGED_IN] ?: false }

    val userLongitudeFlow: Flow<Int> = getFlow { it[USER_LONGITUDE] ?: 0  }

    val userLatitudeFlow: Flow<Int>  = getFlow { it[USER_LATITUDE] ?: 0  }


    suspend fun setUserId(userId: String) {
        dataStore.edit { it[USER_ID] = userId }
    }

    suspend fun setIsUserLoggedIn(isUserLoggedIn: Boolean) {
        dataStore.edit { it[IS_USER_LOGGED_IN] = isUserLoggedIn }
    }

    suspend fun setUserLongitude(userLongitude: Int) {
        dataStore.edit { it[USER_LONGITUDE] = userLongitude }
    }

    suspend fun setUserLatitude(userLatitude: Int) {
        dataStore.edit { it[USER_LATITUDE] = userLatitude }
    }

    private fun <T> getFlow(action: (Preferences) -> T): Flow<T> {
        return dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { action(it) }
    }

    companion object {
        private val USER_ID = preferencesKey<String>("userId")
        private val IS_USER_LOGGED_IN = preferencesKey<Boolean>("isUserLoggedIn")
        private val USER_LONGITUDE = preferencesKey<Int>("userLongitude")
        private val USER_LATITUDE = preferencesKey<Int>("userLatitude")
    }
}