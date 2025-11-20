package com.amrsmh.wiki_repo_amr_smh.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Room para persistir objetos recuperados (Loot).
 * Incluye índices recomendados (createdAt, isFavorite).
 */
@Entity(
    tableName = "loot_items",
    indices = [
        Index(value = ["createdAt"]),
        Index(value = ["isFavorite"])
    ]
)
data class LootEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val category: String,
    val rarity: String,
    val value: Int,
    val weight: Float,
    val transportDifficulty: Int,
    val state: String,
    val runId: String?,
    val notes: String?,
    val createdAt: Long,
    val isFavorite: Boolean
)
