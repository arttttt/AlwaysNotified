package com.arttttt.profiles.impl.domain.repository

import com.arttttt.profiles.api.Profile
import com.arttttt.profiles.api.SelectedActivity

interface ProfilesRepository {

    suspend fun getProfiles(): List<Profile>
    suspend fun saveProfile(
        profile: Profile,
        selectedActivities: List<SelectedActivity>,
    )

    suspend fun removeProfileByUUID(uuid: String)

    suspend fun doesProfileExist(title: String): Boolean
}