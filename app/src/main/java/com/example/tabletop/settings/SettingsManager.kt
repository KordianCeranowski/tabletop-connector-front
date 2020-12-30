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
import net.alexandroid.utils.mylogkt.logV

import java.io.IOException

class SettingsManager(context: Context) {

    private val dataStore = context.createDataStore(name = "settings")

    val isFirstRunFlow: Flow<Boolean> = getFlow { it[IS_FIRST_RUN] ?: true }

    val userAccessTokenFlow: Flow<String> = getFlow { it[USER_ACCESS_TOKEN] ?: ""  }

    val userIdFlow: Flow<String> = getFlow { it[USER_ID] ?: ""  }

    val userLongitudeFlow: Flow<Int> = getFlow { it[USER_LONGITUDE] ?: 0  }

    val userLatitudeFlow: Flow<Int>  = getFlow { it[USER_LATITUDE] ?: 0  }

    suspend fun setIsFirstRun(isFirstRun: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_RUN] = isFirstRun.also { logV("Is First Run : \"$it\"") }
        }
    }

    suspend fun setUserAccessToken(userAccessToken: String) {
        dataStore.edit { preferences ->
            val accessToken = if (userAccessToken.isEmpty()) "" else "Token $userAccessToken"
            preferences[USER_ACCESS_TOKEN] = accessToken.also {
                logV("Access token : \"$it\"")
            }
        }
    }

    suspend fun setUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = userId.also { logV("User id : \"$it\"") }
        }
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
        private val IS_FIRST_RUN = preferencesKey<Boolean>("isFirstRun")
        private val USER_ACCESS_TOKEN = preferencesKey<String>("userAccessToken")
        private val USER_ID = preferencesKey<String>("userId")
        private val USER_LONGITUDE = preferencesKey<Int>("userLongitude")
        private val USER_LATITUDE = preferencesKey<Int>("userLatitude")
    }
}