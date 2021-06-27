package com.mobikasa.pagingsampleremote.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: String,
    val prevKey: Long?,
    val nextKey: Long?,
    val updateAt: Long
)
