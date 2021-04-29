package com.darekbx.legopartscount.repository.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "defined_part")
class DefinedPart(
    @PrimaryKey @ColumnInfo(name = "part_number") val partNumber: Int,
)
