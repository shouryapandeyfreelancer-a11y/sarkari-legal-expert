package com.example.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatHistoryDao {
    @Query("SELECT * FROM chat_history ORDER BY timestamp ASC")
    fun getChatHistory(): Flow<List<ChatHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(item: ChatHistoryItem)

    @Query("DELETE FROM chat_history")
    suspend fun clearChatHistory()
}

@Dao
interface SavedDraftDao {
    @Query("SELECT * FROM saved_drafts ORDER BY timestamp DESC")
    fun getAllDrafts(): Flow<List<SavedDraft>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDraft(draft: SavedDraft)

    @Query("DELETE FROM saved_drafts WHERE id = :id")
    suspend fun deleteDraft(id: Int)
}

@Dao
interface BookmarkedLawDao {
    @Query("SELECT * FROM bookmarked_laws ORDER BY timestamp DESC")
    fun getBookmarkedLaws(): Flow<List<BookmarkedLaw>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmarkLaw(law: BookmarkedLaw)

    @Query("DELETE FROM bookmarked_laws WHERE title = :title")
    suspend fun unbookmarkLawByTitle(title: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_laws WHERE title = :title LIMIT 1)")
    fun isBookmarked(title: String): Flow<Boolean>
}
