package com.arttttt.alwaysnotified.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arttttt.alwaysnotified.data.database.dao.ProfilesDao
import com.arttttt.alwaysnotified.data.database.migrations.Migration_1_2
import com.arttttt.alwaysnotified.data.database.model.ActivityDbModel
import com.arttttt.alwaysnotified.data.database.model.ProfileDbModel

@Database(
    version = 2,
    entities = [
        ActivityDbModel::class,
        ProfileDbModel::class,
    ],
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DB_NAME = "app_db"

        fun create(context: Context): AppDatabase {
            return Room
                .databaseBuilder(
                    context = context,
                    klass = AppDatabase::class.java,
                    name = DB_NAME
                )
                .addMigrations(
                    Migration_1_2,
                )
                .fallbackToDestructiveMigration()
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }
    }

    abstract fun profilesDao(): ProfilesDao
}