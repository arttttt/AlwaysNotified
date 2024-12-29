package com.arttttt.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    primaryKeys = [
        "pkg",
    ]
)
data class AppDbModel(
    @ColumnInfo("pkg")
    val pkg: String
)