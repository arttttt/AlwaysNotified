package com.arttttt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arttttt.database.dao.AppsDao
import com.arttttt.database.model.AppDbModel

@Database(
    version = 4,
    entities = [
        AppDbModel::class,
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
                .fallbackToDestructiveMigration(true)
                .build()
        }
    }

    abstract fun appsDao(): AppsDao
}