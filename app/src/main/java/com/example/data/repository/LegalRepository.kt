package com.example.data.repository

import com.example.BuildConfig
import com.example.data.database.*
import com.example.data.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LegalRepository(
    private val chatDao: ChatHistoryDao,
    private val draftDao: SavedDraftDao,
    private val bookmarkDao: BookmarkedLawDao
) {
    // Database Chat History
    val chatHistory: Flow<List<ChatHistoryItem>> = chatDao.getChatHistory()

    suspend fun addChatMessage(role: String, message: String) = withContext(Dispatchers.IO) {
        chatDao.insertMessage(ChatHistoryItem(role = role, message = message))
    }

    suspend fun clearChatHistory() = withContext(Dispatchers.IO) {
        chatDao.clearChatHistory()
    }

    // Database Saved Drafts
    val savedDrafts: Flow<List<SavedDraft>> = draftDao.getAllDrafts()

    suspend fun saveDraft(title: String, category: String, content: String, fieldsJson: String = "") = withContext(Dispatchers.IO) {
        draftDao.saveDraft(SavedDraft(title = title, category = category, content = content, fieldsJson = fieldsJson))
    }

    suspend fun deleteDraft(id: Int) = withContext(Dispatchers.IO) {
        draftDao.deleteDraft(id)
    }

    // Database Bookmarked Laws
    val bookmarkedLaws: Flow<List<BookmarkedLaw>> = bookmarkDao.getBookmarkedLaws()

    fun isLawBookmarked(title: String): Flow<Boolean> = bookmarkDao.isBookmarked(title)

    suspend fun bookmarkLaw(title: String, description: String, category: String, actYear: String) = withContext(Dispatchers.IO) {
        bookmarkDao.bookmarkLaw(BookmarkedLaw(title = title, description = description, category = category, actYear = actYear))
    }

    suspend fun unbookmarkLaw(title: String) = withContext(Dispatchers.IO) {
        bookmarkDao.unbookmarkLawByTitle(title)
    }

    // Gemini API Direct Integration
    suspend fun getGeminiResponse(
        prompt: String,
        systemInstruction: String? = null,
        historyItems: List<ChatHistoryItem> = emptyList()
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(Exception("API Key is missing or not configured. Please configure GEMINI_API_KEY in the Secrets panel."))
        }

        // Map Chat History into Gemini REST API Content models
        val contents = mutableListOf<Content>()
        historyItems.forEach { history ->
            val role = if (history.role == "user") "user" else "model"
            contents.add(
                Content(
                    parts = listOf(Part(text = history.message)),
                    role = role
                )
            )
        }
        // Add current prompt
        contents.add(Content(parts = listOf(Part(text = prompt)), role = "user"))

        val sysInstructionContent = systemInstruction?.let {
            Content(parts = listOf(Part(text = it)))
        }

        val request = GenerateContentRequest(
            contents = contents,
            systemInstruction = sysInstructionContent
        )

        try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (text != null) {
                Result.success(text)
            } else {
                Result.failure(Exception("Model returned empty or invalid response. Check API logs."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
