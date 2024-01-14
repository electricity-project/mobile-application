package com.electricity.project.api.token


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val AUTHORIZATION_BEARER_TOKEN = "Authorization-Bearer-Token"
private const val REFRESH_BEARER_TOKEN = "Refresh-Bearer-Token"

class TokenManager(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("TokenManager")
    }

    fun getToken(tokenTypes: TokenTypes): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[tokenTypes.tokenKey]
        }
    }

    suspend fun saveToken(tokenTypes: TokenTypes, token: String) {
        context.dataStore.edit { preferences ->
            preferences[tokenTypes.tokenKey] = token
        }
    }

    suspend fun deleteToken(tokenTypes: TokenTypes) {
        context.dataStore.edit { preferences ->
            preferences.remove(tokenTypes.tokenKey)
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences.remove(TokenTypes.AUTHORIZATION_BEARER_TOKEN_KEY.tokenKey)
            preferences.remove(TokenTypes.REFRESH_BEARER_TOKEN_KEY.tokenKey)
        }

        context.dataStore.edit {
            it.clear()
        }
    }
}

enum class TokenTypes(val tokenKey: Preferences.Key<String>) {
    AUTHORIZATION_BEARER_TOKEN_KEY(stringPreferencesKey(AUTHORIZATION_BEARER_TOKEN)),
    REFRESH_BEARER_TOKEN_KEY(stringPreferencesKey(REFRESH_BEARER_TOKEN))
}