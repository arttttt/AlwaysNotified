package com.arttttt.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("ClassName")
object Migration_2_3 : Migration(2, 3) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("drop table if exists profiles_table")
        db.execSQL("ALTER TABLE activities_table RENAME TO activities_table_old;");
        db.execSQL("CREATE TABLE IF NOT EXISTS `activities_table` (`uuid` TEXT NOT NULL, `pkg` TEXT NOT NULL, `activity` TEXT NOT NULL, `manual_mode` INTEGER NOT NULL, PRIMARY KEY(`uuid`))")
        db.execSQL("INSERT INTO activities_table (uuid, pkg, activity, manual_mode) SELECT pkg || '/' || activity, pkg, activity, manual_mode FROM activities_table_old")
        db.execSQL("drop table if exists activities_table_old")
    }
}