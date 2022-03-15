package com.example.af.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.af.App

/**
 * Created by mah on 2022/3/11.
 */
object DB {
    val db by lazy {
        Room.databaseBuilder(
            App.app,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}

@Database(entities = [House::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun houseDao(): HouseDao
}