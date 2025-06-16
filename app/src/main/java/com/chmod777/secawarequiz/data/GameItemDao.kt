package com.chmod777.secawarequiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameItem(item: GameItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGameItems(items: List<GameItem>)

    @Query("SELECT * FROM phishing_game_items")
    fun getAllGameItems(): Flow<List<GameItem>>

    @Query("SELECT * FROM phishing_game_items WHERE id = :id")
    suspend fun getGameItemById(id: Int): GameItem?

    @Query("DELETE FROM phishing_game_items")
    suspend fun deleteAllGameItems()
}
