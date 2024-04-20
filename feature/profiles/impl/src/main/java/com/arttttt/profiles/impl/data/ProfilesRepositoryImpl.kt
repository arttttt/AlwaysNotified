package com.arttttt.profiles.impl.data

import com.arttttt.database.dao.ProfilesDao
import com.arttttt.database.model.ActivityDbModel
import com.arttttt.database.model.ProfileDbModel
import com.arttttt.database.model.ProfileWithActivities
import com.arttttt.profiles.api.Profile
import com.arttttt.appslist.SelectedActivity
import com.arttttt.profiles.api.ProfilesRepository
import java.util.UUID

internal class ProfilesRepositoryImpl(
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

    override suspend fun doesProfileExist(title: String): Boolean {
        return profilesDao.doesProfileExist(title)
    }
}