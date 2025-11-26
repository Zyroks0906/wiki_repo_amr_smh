package com.amrsmh.wiki_repo_amr_smh.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shop_items",
    indices = [
        Index(value = ["price"]),
        Index(value = ["isFavorite"])
    ]
)
data class ShopItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val price: Int,
    val description: String,
    val category: String,
    val createdAt: Long,
    val isFavorite: Boolean
)