package com.amrsmh.wiki_repo_amr_smh.domain.models

/**
 * Modelo de dominio para UI (no depende de Room).
 * Usado por ViewModels y Composables.
 */
data class LootItem(
    val id: Long = 0L,
    val name: String,
    val category: String,
    val rarity: String,
    val value: Int,
    val weight: Float,
    val transportDifficulty: Int,
    val state: String,
    val runId: String? = null,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
