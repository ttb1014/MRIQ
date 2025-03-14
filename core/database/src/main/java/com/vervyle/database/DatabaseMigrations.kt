package com.vervyle.database

import androidx.room.Delete
import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

internal object DatabaseMigrations {

    @DeleteTable(tableName = "MriData")
    class Schema1to2 : AutoMigrationSpec
}