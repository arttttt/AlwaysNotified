package com.arttttt.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithActivities(
    @Embedded val profile: ProfileDbModel,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "profile_uuid"
    )
    val activities: List<ActivityDbModel>
)