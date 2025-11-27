package com.amrsmh.wiki_repo_amr_smh.data.repository

import com.amrsmh.wiki_repo_amr_smh.data.local.dao.MonsterDao
import com.amrsmh.wiki_repo_amr_smh.data.local.entities.MonsterEntity
import com.amrsmh.wiki_repo_amr_smh.domain.models.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MonsterRepository(private val dao: MonsterDao) {

    fun observeAll(): Flow<List<Monster>> =
        dao.getAllFlow().map { entities -> entities.map { it.toDomain() } }

    fun observeFavorites(): Flow<List<Monster>> =
        dao.getFavoritesFlow().map { entities -> entities.map { it.toDomain() } }

    fun observeById(id: Long): Flow<Monster?> =
        dao.getByIdFlow(id).map { entity -> entity?.toDomain() }

    suspend fun add(monster: Monster) {
        dao.insert(monster.toEntity())
    }

    suspend fun update(monster: Monster) {
        dao.update(monster.toEntity())
    }

    suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    private fun MonsterEntity.toDomain() = Monster(
        id = id,
        name = name,
        danger = danger,
        detection = detection,
        notes = notes,
        createdAt = createdAt,
        isFavorite = isFavorite
    )

    private fun Monster.toEntity() = MonsterEntity(
        id = id,
        name = name,
        danger = danger,
        detection = detection,
        notes = notes,
        createdAt = createdAt,
        isFavorite = isFavorite
    )
}