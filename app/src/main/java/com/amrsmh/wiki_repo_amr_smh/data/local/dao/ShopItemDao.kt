package com.amrsmh.wiki_repo_amr_smh.data.local.dao

import androidx.room.*
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.ShopItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopItemDao {
    @Query("SELECT * FROM shop_items ORDER BY price ASC, createdAt DESC")
    fun getAllFlow(): Flow<List<ShopItemEntity>>

    @Query("SELECT * FROM shop_items WHERE isFavorite = 1 ORDER BY price ASC")
    fun getFavoritesFlow(): Flow<List<ShopItemEntity>>

    @Query("SELECT * FROM shop_items WHERE id = :id LIMIT 1")
    fun getByIdFlow(id: Long): Flow<ShopItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ShopItemEntity)

    @Update
    suspend fun update(entity: ShopItemEntity)

    @Delete
    suspend fun delete(entity: ShopItemEntity)

    @Query("DELETE FROM shop_items WHERE id = :id")
    suspend fun deleteById(id: Long)
}