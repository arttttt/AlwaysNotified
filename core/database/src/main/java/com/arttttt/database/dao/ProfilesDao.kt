package com.arttttt.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arttttt.database.model.ActivityDbModel
import kotlinx.coroutines.runBlocking

@Dao
abstract class ProfilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertActivities(vararg activities: ActivityDbModel)

    @Query("delete from activities_table where uuid = :uuid")
    abstract suspend fun removeActivity(uuid: String)

    @Query("select * from activities_table")
    abstract suspend fun getSelectedActivities(): List<ActivityDbModel>

    @Transaction
    open fun removeActivities(activities: List<ActivityDbModel>) {
        runBlocking {
            activities.forEach { activity ->
                removeActivity(activity.uuid)
            }
        }
    }
}