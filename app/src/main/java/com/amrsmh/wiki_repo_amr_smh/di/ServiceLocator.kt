package com.amrsmh.wiki_repo_amr_smh.di

import android.content.Context
import com.amrsmh.wiki_repo_amr_smh.data.datastore.PreferencesManager
import com.amrsmh.wiki_repo_amr_smh.data.local.RoomDb
import com.amrsmh.wiki_repo_amr_smh.data.repository.LootRepository
import com.amrsmh.wiki_repo_amr_smh.data.repository.MonsterRepository
import com.amrsmh.wiki_repo_amr_smh.data.repository.ShopItemRepository

object ServiceLocator {

    private var database: RoomDb? = null
    private var prefs: PreferencesManager? = null
    private var lootRepo: LootRepository? = null
    private var monsterRepo: MonsterRepository? = null
    private var shopRepo: ShopItemRepository? = null

    fun init(context: Context) {
        database = RoomDb.getInstance(context)
        prefs = PreferencesManager(context)
        lootRepo = LootRepository(database!!.lootDao())
        monsterRepo = MonsterRepository(database!!.monsterDao())
        shopRepo = ShopItemRepository(database!!.shopItemDao())
    }

    fun providePreferencesManager(): PreferencesManager {
        return prefs ?: throw IllegalStateException("ServiceLocator not initialized")
    }

    fun provideLootRepository(): LootRepository {
        return lootRepo ?: throw IllegalStateException("ServiceLocator not initialized")
    }

    fun provideMonsterRepository(): MonsterRepository {
        return monsterRepo ?: throw IllegalStateException("ServiceLocator not initialized")
    }

    fun provideShopItemRepository(): ShopItemRepository {
        return shopRepo ?: throw IllegalStateException("ServiceLocator not initialized")
    }
}