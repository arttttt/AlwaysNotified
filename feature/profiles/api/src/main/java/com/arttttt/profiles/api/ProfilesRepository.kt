package com.arttttt.profiles.api

import com.arttttt.appslist.SelectedActivity

interface ProfilesRepository {

    suspend fun getProfiles(): List<Profile>
    suspend fun saveProfile(
        profile: Profile,
        selectedActivities: List<SelectedActivity>,
    )

    suspend fun removeProfileByUUID(uuid: String)

    suspend fun doesProfileExist(title: String): Boolean
}