package com.amrsmh.wiki_repo_amr_smh.data.repository

import com.amrsmh.wiki_repo_amr_smh.data.local.dao.LootDao
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.LootEntity
import com.amrsmh.wiki_repo_amr_smh.domain.models.LootItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository: hace mapping Entity <-> Domain (LootItem).
 * Expone Flows para la UI y funciones suspend para escrituras.
 */
class LootRepository(private val dao: LootDao) {

    fun observeAll(): Flow<List<LootItem>> =
        dao.getAllFlow().map { list -> list.map { it.toDomain() } }

    fun observeFavorites(): Flow<List<LootItem>> =
        dao.getFavoritesFlow().map { list -> list.map { it.toDomain() } }

    fun observeById(id: Long): Flow<LootItem?> =
        dao.getByIdFlow(id).map { it?.toDomain() }

    suspend fun add(item: LootItem) {
        dao.insert(item.toEntity())
    }

    suspend fun update(item: LootItem) {
        dao.update(item.toEntity())
    }

    suspend fun delete(id: Long) {
        dao.deleteById(id)
    }
}

/* Mappers */
private fun LootEntity.toDomain() = LootItem(
    id = id,
    name = name,
    category = category,
    rarity = rarity,
    value = value,
    weight = weight,
    transportDifficulty = transportDifficulty,
    state = state,
    runId = runId,
    notes = notes,
    createdAt = createdAt,
    isFavorite = isFavorite
)

private fun LootItem.toEntity() = LootEntity(
    id = id,
    name = name,
    category = category,
    rarity = rarity,
    value = value,
    weight = weight,
    transportDifficulty = transportDifficulty,
    state = state,
    runId = runId,
    notes = notes,
    createdAt = createdAt,
    isFavorite = isFavorite
)
