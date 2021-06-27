package com.mobikasa.pagingsampleremote.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "result_data")
data class Results(
    @PrimaryKey val title: String,
    val id: Long,
    val poster_path: String?,
)

