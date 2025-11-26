package com.amrsmh.wiki_repo_amr_smh.data.local.dao

import androidx.room.*
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.MonsterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonsterDao {

    @Query("SELECT * FROM monsters ORDER BY danger DESC, createdAt DESC")
    fun getAllFlow(): Flow<List<MonsterEntity>>

    @Query("SELECT * FROM monsters WHERE isFavorite = 1 ORDER BY danger DESC")
    fun getFavoritesFlow(): Flow<List<MonsterEntity>>

    @Query("SELECT * FROM monsters WHERE id = :id LIMIT 1")
    fun getByIdFlow(id: Long): Flow<MonsterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MonsterEntity)

    @Update
    suspend fun update(entity: MonsterEntity)

    @Delete
    suspend fun delete(entity: MonsterEntity)

    @Query("DELETE FROM monsters WHERE id = :id")
    suspend fun deleteById(id: Long)
}