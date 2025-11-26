package com.amrsmh.wiki_repo_amr_smh.data.repository

import com.amrsmh.wiki_repo_amr_smh.data.local.dao.ShopItemDao
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.ShopItemEntity
import com.amrsmh.wiki_repo_amr_smh.domain.models.ShopItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShopItemRepository(private val dao: ShopItemDao) {

    fun observeAll(): Flow<List<ShopItem>> =
        dao.getAllFlow().map { list -> list.map { entity -> entity.toDomain() } }

    fun observeFavorites(): Flow<List<ShopItem>> =
        dao.getFavoritesFlow().map { list -> list.map { entity -> entity.toDomain() } }

    fun observeById(id: Long): Flow<ShopItem?> =
        dao.getByIdFlow(id).map { entity -> entity?.toDomain() }

    suspend fun add(item: ShopItem) {
        dao.insert(item.toEntity())
    }

    suspend fun update(item: ShopItem) {
        dao.update(item.toEntity())
    }

    suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    private fun ShopItemEntity.toDomain() = ShopItem(
        id = this.id,
        name = this.name,
        price = this.price,
        description = this.description,
        category = this.category,
        createdAt = this.createdAt,
        isFavorite = this.isFavorite
    )

    private fun ShopItem.toEntity() = ShopItemEntity(
        id = this.id,
        name = this.name,
        price = this.price,
        description = this.description,
        category = this.category,
        createdAt = this.createdAt,
        isFavorite = this.isFavorite
    )
}