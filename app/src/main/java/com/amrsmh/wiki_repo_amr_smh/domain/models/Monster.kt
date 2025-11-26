package com.amrsmh.wiki_repo_amr_smh.domain.models

/**
 * Modelo de dominio para monstruos del bestiario.
 * Usado por ViewModels y Composables.
 */
data class Monster(
    val id: Long = 0L,
    val name: String,
    val danger: Int,
    val detection: String,
    val notes: String,
    val weaknesses: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)