package com.chmod777.secawarequiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FakeLoginGameItemDao {
    @Query("SELECT * FROM fake_login_game_items")
    fun getAllItems(): Flow<List<FakeLoginGameItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<FakeLoginGameItem>)

    @Query("DELETE FROM fake_login_game_items")
    suspend fun deleteAllItems()
}
