package com.arttttt.appholder.data.repository

import com.arttttt.appholder.data.database.dao.ProfilesDao
import com.arttttt.appholder.data.database.model.ActivityDbModel
import com.arttttt.appholder.data.database.model.ProfileDbModel
import com.arttttt.appholder.data.database.model.ProfileWithActivities
import com.arttttt.appholder.domain.entity.profiles.Profile
import com.arttttt.appholder.domain.entity.profiles.SelectedActivity
import com.arttttt.appholder.domain.repository.ProfilesRepository
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
                        activity = activity.activity,
                        profileUuid = profile.uuid,
                    )
                }
            )
        )
    }

    override suspend fun removeProfileByUUID(uuid: String) {
        profilesDao.removeProfileByUUID(uuid)
    }
}