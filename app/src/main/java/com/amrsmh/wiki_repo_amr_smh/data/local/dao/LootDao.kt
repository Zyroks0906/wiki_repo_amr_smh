package com.amrsmh.wiki_repo_amr_smh.data.local.dao

import androidx.room.*
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.LootEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO con Flows para observación reactiva.
 */
@Dao
interface LootDao {

    @Query("SELECT * FROM loot_items ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<LootEntity>>

    @Query("SELECT * FROM loot_items WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoritesFlow(): Flow<List<LootEntity>>

    @Query("SELECT * FROM loot_items WHERE id = :id LIMIT 1")
    fun getByIdFlow(id: Long): Flow<LootEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LootEntity)

    @Update
    suspend fun update(entity: LootEntity)

    @Delete
    suspend fun delete(entity: LootEntity)

    @Query("DELETE FROM loot_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}
