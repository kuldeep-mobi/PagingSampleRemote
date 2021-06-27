package com.mobikasa.pagingsampleremote.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mobikasa.pagingsampleremote.models.RemoteKey
import com.mobikasa.pagingsampleremote.models.Results

@Database(entities = [Results::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getResultDao(): ResultDao
    abstract fun getRemoteDao(): RemoteDao

    companion object {
        @Volatile
        private var mInstance: MyDatabase? = null
        private val lock = Any()

        operator fun invoke(mContext: Context) = mInstance ?: synchronized(lock) {
            buildDataBase(mContext).also {
                mInstance = it
            }
        }

        private fun buildDataBase(mContext: Context) =
            Room.databaseBuilder(
                mContext.applicationContext,
                MyDatabase::class.java,
                "movies-db"
            ).build()
    }
}