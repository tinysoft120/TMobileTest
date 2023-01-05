package com.tinysoft.tmobiletest.db

import androidx.room.*
import java.util.*

@Database(entities = [CardRow::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}