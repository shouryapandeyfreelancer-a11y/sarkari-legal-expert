package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_history")
data class ChatHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String, // "user" or "assistant"
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_drafts")
data class SavedDraft(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val content: String,
    val fieldsJson: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarked_laws")
data class BookmarkedLaw(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val category: String,
    val actYear: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
