package com.arttttt.appholder.domain.repository

import com.arttttt.appholder.domain.entity.profiles.Profile
import com.arttttt.appholder.domain.entity.profiles.SelectedActivity

interface ProfilesRepository {

    suspend fun getProfiles(): List<Profile>
    suspend fun saveProfile(
        profile: Profile,
        selectedActivities: List<SelectedActivity>,
    )

    suspend fun removeProfileByUUID(uuid: String)
}