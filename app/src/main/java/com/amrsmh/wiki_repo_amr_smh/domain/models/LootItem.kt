package com.amrsmh.wiki_repo_amr_smh.domain.models

/**
 * Modelo de dominio para UI (no depende de Room).
 * Usado por ViewModels y Composables.
 */
data class LootItem(
    val id: Long = 0L,
    val name: String,
    val location: String, // "Genérico", "Mágico", "Mansión", "Ártico", "Museo"
    val value: Int,
    val weight: String, // "Ligero", "Normal", "Pesado", "Muy Pesado"
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)