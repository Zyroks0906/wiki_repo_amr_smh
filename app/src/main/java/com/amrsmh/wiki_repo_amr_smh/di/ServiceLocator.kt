package com.amrsmh.wiki_repo_amr_smh.di

import android.content.Context
import com.amrsmh.wiki_repo_amr_smh.data.datastore.PreferencesManager
import com.amrsmh.wiki_repo_amr_smh.data.local.RoomDb
import com.amrsmh.wiki_repo_amr_smh.data.repository.LootRepository

/**
 * ServiceLocator simple: inicializar desde MainActivity.
 * Provee repository y preferences manager.
 */
object ServiceLocator {

    private var database: RoomDb? = null
    private var prefs: PreferencesManager? = null
    private var repo: LootRepository? = null

    fun init(context: Context) {
        database = RoomDb.getInstance(context)
        prefs = PreferencesManager(context)
        repo = LootRepository(database!!.lootDao())
    }

    fun providePreferencesManager(): PreferencesManager {
        return prefs ?: throw IllegalStateException("ServiceLocator not initialized")
    }

    fun provideLootRepository(): LootRepository {
        return repo ?: throw IllegalStateException("ServiceLocator not initialized")
    }
}
