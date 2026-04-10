package com.simple.sportly.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.simple.sportly.domain.model.AuthSession
import com.simple.sportly.domain.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class SessionStore(
    private val context: Context
) {
    private val tokenKey = stringPreferencesKey("token")
    private val roleKey = stringPreferencesKey("role")

    val session: Flow<AuthSession?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val token = preferences[tokenKey]
            val role = preferences[roleKey]
            if (token.isNullOrBlank() || role.isNullOrBlank()) {
                null
            } else {
                AuthSession(
                    token = token,
                    role = UserRole.fromApi(role)
                )
            }
        }

    suspend fun saveSession(session: AuthSession) {
        context.dataStore.edit { preferences ->
            preferences[tokenKey] = session.token
            preferences[roleKey] = session.role.name
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(tokenKey)
            preferences.remove(roleKey)
        }
    }
}
