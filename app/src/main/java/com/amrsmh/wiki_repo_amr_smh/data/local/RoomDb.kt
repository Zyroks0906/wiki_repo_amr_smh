package com.amrsmh.wiki_repo_amr_smh.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.LootDao
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.MonsterDao
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.ShopItemDao
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.LootEntity
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.MonsterEntity
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.ShopItemEntity

@Database(
    entities = [
        LootEntity::class,
        MonsterEntity::class,
        ShopItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RoomDb : RoomDatabase() {
    abstract fun lootDao(): LootDao
    abstract fun monsterDao(): MonsterDao
    abstract fun shopItemDao(): ShopItemDao

    companion object {
        @Volatile private var INSTANCE: RoomDb? = null

        fun getInstance(context: Context): RoomDb {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDb::class.java,
                    "repo_database.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = inst
                inst
            }
        }
    }
}