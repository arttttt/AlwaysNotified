package com.arttttt.alwaysnotified.data.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("ClassName")
object Migration_1_2 : Migration(1, 2) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE activities_table ADD COLUMN manual_mode INTEGER NOT NULL DEFAULT 0")
    }
}