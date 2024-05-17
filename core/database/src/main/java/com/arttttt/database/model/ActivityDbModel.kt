package com.arttttt.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "activities_table",
    primaryKeys = [
        "pkg",
    ],
)
data class ActivityDbModel(
    val pkg: String,
    val activity: String,
    @ColumnInfo("manual_mode") val manualMode: Boolean,
)