package com.arttttt.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("ClassName")
object Migration_3_4 : Migration(3, 4) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE activities_table RENAME TO activities_table_old;");
        db.execSQL("CREATE TABLE IF NOT EXISTS `activities_table` (`pkg` TEXT NOT NULL, `activity` TEXT NOT NULL, `manual_mode` INTEGER NOT NULL, PRIMARY KEY(`pkg`))")
        db.execSQL("INSERT INTO activities_table (pkg, activity, manual_mode) SELECT pkg, activity, manual_mode FROM activities_table_old")
        db.execSQL("drop table if exists activities_table_old")
    }
}