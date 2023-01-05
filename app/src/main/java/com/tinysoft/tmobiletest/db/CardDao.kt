package com.tinysoft.tmobiletest.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("DELETE FROM card_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storeAllCards(cards: List<CardRow>)

    @Query("SELECT * FROM card_table")
    suspend fun getAllCards(): List<CardRow>
}