package com.arttttt.profiles.api

interface ProfilesRepository {

    suspend fun getProfiles(): List<com.arttttt.alwaysnotified.Profile>
    suspend fun saveProfile(
        profile: com.arttttt.alwaysnotified.Profile,
        selectedActivities: List<com.arttttt.alwaysnotified.SelectedActivity>,
    )

    suspend fun removeProfileByUUID(uuid: String)

    suspend fun isProfileExist(title: String): Boolean
}