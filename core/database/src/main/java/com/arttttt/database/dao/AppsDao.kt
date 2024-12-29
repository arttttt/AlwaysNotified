package com.arttttt.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arttttt.database.model.AppDbModel

@Dao
interface AppsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveApp(app: AppDbModel)

    @Delete
    suspend fun removeApp(app: AppDbModel)

    @Query("SELECT * FROM AppDbModel")
    suspend fun getAllApps(): List<AppDbModel>
}