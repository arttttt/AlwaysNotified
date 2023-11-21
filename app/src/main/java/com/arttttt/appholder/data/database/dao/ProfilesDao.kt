package com.arttttt.appholder.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arttttt.appholder.data.database.model.ActivityDbModel
import com.arttttt.appholder.data.database.model.ProfileDbModel
import com.arttttt.appholder.data.database.model.ProfileWithActivities
import kotlinx.coroutines.runBlocking

@Dao
abstract class ProfilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertProfile(vararg profile: ProfileDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertActivities(vararg activities: ActivityDbModel)

    @Query("select * from profiles_table")
    abstract suspend fun getProfiles(): List<ProfileDbModel>

    @Query("select * from activities_table where profile_uuid = :uuid")
    abstract suspend fun getSelectedActivitiesForUuid(uuid: String): List<ActivityDbModel>

    @Query("delete from activities_table where profile_uuid = :uuid")
    abstract suspend fun clearActivitiesForUuid(uuid: String)

    @Query("delete from profiles_table where uuid = :uuid")
    abstract fun removeProfileByUUID(uuid: String)

    @Transaction
    open fun insertProfile(profileWithActivities: ProfileWithActivities) {
        runBlocking {
            insertProfile(profileWithActivities.profile)
            clearActivitiesForUuid(profileWithActivities.profile.uuid)
            insertActivities(*profileWithActivities.activities.toTypedArray())
        }
    }
}