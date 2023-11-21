package com.arttttt.appholder.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "activities_table",
    primaryKeys = [
        "uuid",
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProfileDbModel::class,
            parentColumns = [
                "uuid"
            ],
            childColumns = [
                "profile_uuid"
            ],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.NO_ACTION,
        )
    ],
    indices = [
        Index(
            "profile_uuid"
        )
    ],
)
data class ActivityDbModel(
    val uuid: String,
    val pkg: String,
    val activity: String,
    @ColumnInfo("profile_uuid") val profileUuid: String?,
)