package com.arttttt.profiles.api

import com.arttttt.alwaysnotified.Profile
import com.arttttt.alwaysnotified.SelectedActivity

interface ProfilesRepository {

    suspend fun getProfiles(): List<Profile>
    suspend fun saveProfile(
        profile: Profile,
        selectedActivities: List<SelectedActivity>,
    )

    suspend fun removeProfileByUUID(uuid: String)

    suspend fun isProfileExist(title: String): Boolean
}