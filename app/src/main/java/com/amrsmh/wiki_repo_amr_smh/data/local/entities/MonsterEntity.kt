package com.amrsmh.wiki_repo_amr_smh.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "monsters",
    indices = [
        Index(value = ["danger"]),
        Index(value = ["isFavorite"])
    ]
)
data class MonsterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val danger: Int,
    val detection: String,
    val notes: String,
    val createdAt: Long,
    val isFavorite: Boolean
)