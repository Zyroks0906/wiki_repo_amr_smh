package com.amrsmh.wiki_repo_amr_smh.domain.models

data class ShopItem(
    val id: Long = 0L,
    val name: String,
    val price: Int,
    val description: String,
    val category: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)