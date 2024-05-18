package com.arttttt.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("ClassName")
object Migration_2_3 : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        var cursor = db.query("SELECT uuid FROM profiles_table WHERE LOWER(title) = 'default'")
        var defaultProfileUuid: String? = null
        if (cursor.moveToFirst()) {
            defaultProfileUuid = cursor.getString(cursor.getColumnIndexOrThrow("uuid"))
        }
        cursor.close()

        if (defaultProfileUuid == null) {
            cursor = db.query("SELECT uuid FROM profiles_table LIMIT 1")
            if (cursor.moveToFirst()) {
                defaultProfileUuid = cursor.getString(cursor.getColumnIndexOrThrow("uuid"))
            }
            cursor.close()
        }

        db.execSQL("ALTER TABLE activities_table RENAME TO activities_table_old;")
        db.execSQL("CREATE TABLE IF NOT EXISTS `activities_table` (`pkg` TEXT NOT NULL, `activity` TEXT NOT NULL, `manual_mode` INTEGER NOT NULL, PRIMARY KEY(`pkg`))")
        db.execSQL("INSERT INTO activities_table (pkg, activity, manual_mode) SELECT pkg, activity, manual_mode FROM activities_table_old WHERE profile_uuid = '$defaultProfileUuid'")

        db.execSQL("DROP TABLE IF EXISTS activities_table_old")
        db.execSQL("DROP TABLE IF EXISTS profiles_table")
    }
}