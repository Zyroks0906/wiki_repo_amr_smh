package com.amrsmh.wiki_repo_amr_smh.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "repo_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_mode")
        val ORDER_KEY = stringPreferencesKey("list_order")
        val FAVORITES_ONLY_KEY = booleanPreferencesKey("show_favorites")
    }

    val themeFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: "SYSTEM"
    }

    val listOrderFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[ORDER_KEY] ?: "BY_DATE"
    }

    val showFavoritesFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[FAVORITES_ONLY_KEY] ?: false
    }

    suspend fun setTheme(newTheme: String) {
        context.dataStore.edit { it[THEME_KEY] = newTheme }
    }

    suspend fun setListOrder(order: String) {
        context.dataStore.edit { it[ORDER_KEY] = order }
    }

    suspend fun setShowFavorites(enabled: Boolean) {
        context.dataStore.edit { it[FAVORITES_ONLY_KEY] = enabled }
    }
}
