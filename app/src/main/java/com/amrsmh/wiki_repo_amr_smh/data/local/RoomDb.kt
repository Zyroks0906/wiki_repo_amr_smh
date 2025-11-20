package com.amrsmh.wiki_repo_amr_smh.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amrsmh.wiki_repo_amr_smh.data.local.dao.LootDao
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.LootEntity

/**
 * Base de datos Room. Version = 1.
 * Nombre del archivo DB: repo_database.db
 */
@Database(entities = [LootEntity::class], version = 1, exportSchema = false)
abstract class RoomDb : RoomDatabase() {
    abstract fun lootDao(): LootDao

    companion object {
        @Volatile private var INSTANCE: RoomDb? = null

        fun getInstance(context: Context): RoomDb {
            return INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDb::class.java,
                    "repo_database.db"
                ).build()
                INSTANCE = inst
                inst
            }
        }
    }
}
