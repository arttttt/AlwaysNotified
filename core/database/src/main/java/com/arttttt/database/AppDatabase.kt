package com.arttttt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arttttt.database.dao.ProfilesDao
import com.arttttt.database.migrations.Migration_1_2
import com.arttttt.database.model.ActivityDbModel

@Database(
    version = 2,
    entities = [
        ActivityDbModel::class,
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
                //.fallbackToDestructiveMigration()
                //.fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }
    }

    abstract fun profilesDao(): ProfilesDao
}