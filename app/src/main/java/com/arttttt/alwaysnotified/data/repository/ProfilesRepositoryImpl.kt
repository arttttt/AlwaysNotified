package com.arttttt.alwaysnotified.data.repository

import com.arttttt.alwaysnotified.data.database.dao.ProfilesDao
import com.arttttt.alwaysnotified.data.database.model.ActivityDbModel
import com.arttttt.alwaysnotified.data.database.model.ProfileDbModel
import com.arttttt.alwaysnotified.data.database.model.ProfileWithActivities
import com.arttttt.alwaysnotified.Profile
import com.arttttt.alwaysnotified.SelectedActivity
import com.arttttt.profiles.api.ProfilesRepository
import java.util.UUID

class ProfilesRepositoryImpl(
    private val profilesDao: ProfilesDao,
) : ProfilesRepository {

    override suspend fun getProfiles(): List<Profile> {
        return profilesDao
            .getProfiles()
            .map { profile ->
                Profile(
                    uuid = profile.uuid,
                    title = profile.title,
                    color = profile.color,
                )
            }
    }

    override suspend fun saveProfile(
        profile: Profile,
        selectedActivities: List<SelectedActivity>,
    ) {
        profilesDao.insertProfile(
            profileWithActivities = ProfileWithActivities(
                profile = ProfileDbModel(
                    uuid = profile.uuid,
                    title = profile.title,
                    color = profile.color,
                ),
                activities = selectedActivities.map { activity ->
                    ActivityDbModel(
                        uuid = UUID.randomUUID().toString(),
                        pkg = activity.pkg,
                        activity = activity.name,
                        profileUuid = profile.uuid,
                        manualMode = activity.manualMode,
                    )
                }
            )
        )
    }

    override suspend fun removeProfileByUUID(uuid: String) {
        profilesDao.removeProfileByUUID(uuid)
    }

    override suspend fun isProfileExist(title: String): Boolean {
        return profilesDao.doesProfileExist(title)
    }
}