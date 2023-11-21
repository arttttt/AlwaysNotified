package com.arttttt.appholder.data.database.model

import androidx.room.Entity

@Entity(
    tableName = "profiles_table",
    primaryKeys = [
        "uuid",
    ]
)
data class ProfileDbModel(
    val uuid: String,
    val title: String,
    val color: Int,
)