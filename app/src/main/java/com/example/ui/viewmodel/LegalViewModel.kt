package com.example.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.BookmarkedLaw
import com.example.data.database.ChatHistoryItem
import com.example.data.database.SavedDraft
import com.example.data.model.DraftTemplate
import com.example.data.model.DraftTemplates
import com.example.data.repository.LegalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LegalViewModel(private val repository: LegalRepository) : ViewModel() {

    // --- Tab Selection State ---
    val selectedTab = mutableStateOf(0)

    // --- Language Selection State ---
    // 0: English, 1: Hindi (हिंदी)
    val selectedLanguage = mutableStateOf(0)

    // --- Chat / Consultation State ---
    val chatInput = mutableStateOf("")
    val isChatLoading = mutableStateOf(false)
    val chatError = mutableStateOf<String?>(null)

    val chatHistory: StateFlow<List<ChatHistoryItem>> = repository.chatHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun sendChatMessage() {
        val message = chatInput.value.trim()
        if (message.isEmpty() || isChatLoading.value) return

        viewModelScope.launch {
            isChatLoading.value = true
            chatError.value = null
            chatInput.value = ""

            // Save user message in local DB
            repository.addChatMessage("user", message)

            // Get complete chat history for API context
            val history = chatHistory.value

            val systemInstruction = if (selectedLanguage.value == 1) {
                """
                    आप एक अत्यधिक पेशेवर, विनम्र और मददगार सरकारी कानूनी सहायक एआई (Government Legal Assistant AI) हैं।
                    आपका उद्देश्य नागरिकों, शोधकर्ताओं और प्रशासकों को कानूनी अधिनियमों, कानूनों, नीतियों और विनियमों को समझने में सहायता करना है।
                    कृपया पूरी तरह से हिंदी (Hindi) में उत्तर दें। यदि कुछ कानूनी तकनीकी शब्द हों, तो उन्हें कोष्ठक में अंग्रेजी में भी लिख सकते हैं, लेकिन मुख्य विवरण स्पष्ट, सरल देवनागरी हिंदी में होना चाहिए।
                    उचित होने पर प्रासंगिक धारा के नाम, संवैधानिक लेख या अधिनियम के नाम शामिल करें।
                    हमेशा अंत में एक अनुकूल पेशेवर अस्वीकरण (Disclaimer) जोड़ें: 'अस्वीकरण: यह एक एआई-संचालित कानूनी सहायक है और केवल शैक्षिक और सूचनात्मक उद्देश्यों के लिए है। यह औपचारिक कानूनी सलाह नहीं है।'
                """.trimIndent()
            } else {
                """
                    You are a highly professional, polite, and helpful Government Legal Assistant AI.
                    Your purpose is to assist citizens, researchers, and administrators in understanding legal acts, laws, policies, and regulations.
                    Give accurate, factual, and informative responses based on standard public law.
                    Include relevant section names, constitutional articles, or act names when appropriate.
                    Always add a friendly professional disclaimer at the end stating: 'Disclaimer: This is an AI-powered legal assistant and is meant for educational and informational purposes only. It does not constitute formal legal counsel.'
                """.trimIndent()
            }

            val result = repository.getGeminiResponse(
                prompt = message,
                systemInstruction = systemInstruction,
                historyItems = history
            )

            result.fold(
                onSuccess = { responseText ->
                    repository.addChatMessage("assistant", responseText)
                },
                onFailure = { throwable ->
                    chatError.value = throwable.localizedMessage ?: if (selectedLanguage.value == 1) "एआई सेवा से संपर्क करने में विफल" else "Failed to contact AI service"
                }
            )
            isChatLoading.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    // --- Drafting State ---
    val selectedTemplate = mutableStateOf<DraftTemplate>(DraftTemplates.templates.first())
    val draftFields = mutableStateMapOf<String, String>()
    val generatedDraft = mutableStateOf<String?>(null)
    val isDraftLoading = mutableStateOf(false)
    val draftError = mutableStateOf<String?>(null)

    fun selectTemplate(template: DraftTemplate) {
        selectedTemplate.value = template
        draftFields.clear()
        template.fields.forEach { field ->
            draftFields[field.key] = field.defaultValue
        }
        generatedDraft.value = null
        draftError.value = null
    }

    fun generateLegalDraft() {
        val template = selectedTemplate.value
        val fields = draftFields.toMap()

        // Check that all required inputs are present
        val missing = template.fields.filter { fields[it.key]?.trim().isNullOrEmpty() }
        if (missing.isNotEmpty()) {
            draftError.value = if (selectedLanguage.value == 1) {
                "कृपया सभी विवरण भरें: ${missing.joinToString { it.label }}"
            } else {
                "Please fill in all details: ${missing.joinToString { it.label }}"
            }
            return
        }

        viewModelScope.launch {
            isDraftLoading.value = true
            draftError.value = null
            generatedDraft.value = null

            val prompt = template.promptBuilder(fields)
            val systemInstruction = if (selectedLanguage.value == 1) {
                "आप एक विशेषज्ञ कानूनी ड्राफ्टर हैं। स्वच्छ, औपचारिक, पूरी तरह से स्वरूपित कानूनी दस्तावेज तैयार करें। दस्तावेज को हिंदी (Devanagari script) में बनाएं। सारांश चैट या साइड नोट्स न लिखें; केवल संरचित कानूनी दस्तावेज पाठ आउटपुट करें।"
            } else {
                "You are an expert legal drafter. Produce clean, formal, perfectly formatted legal documents. Do not write summary chat or side notes; output ONLY the structured document text."
            }

            val result = repository.getGeminiResponse(
                prompt = prompt,
                systemInstruction = systemInstruction
            )

            result.fold(
                onSuccess = { docText ->
                    generatedDraft.value = docText
                },
                onFailure = { throwable ->
                    draftError.value = throwable.localizedMessage ?: if (selectedLanguage.value == 1) "दस्तावेज़ ड्राफ्ट तैयार करने में विफल" else "Failed to generate draft document"
                }
            )
            isDraftLoading.value = false
        }
    }

    fun saveCurrentDraft() {
        val doc = generatedDraft.value ?: return
        val template = selectedTemplate.value
        viewModelScope.launch {
            repository.saveDraft(
                title = template.title,
                category = template.category,
                content = doc
            )
        }
    }

    // --- Law Hub State ---
    val searchQuery = mutableStateOf("")
    val selectedLawCategory = mutableStateOf("All")

    val bookmarkedLaws: StateFlow<List<BookmarkedLaw>> = repository.bookmarkedLaws
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val bookmarkedTitles: StateFlow<Set<String>> = bookmarkedLaws
        .map { list -> list.map { it.title }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    fun toggleBookmarkLaw(title: String, description: String, category: String, actYear: String) {
        val isCurrentlyBookmarked = bookmarkedTitles.value.contains(title)
        viewModelScope.launch {
            if (isCurrentlyBookmarked) {
                repository.unbookmarkLaw(title)
            } else {
                repository.bookmarkLaw(title, description, category, actYear)
            }
        }
    }

    // --- My Vault State ---
    val savedDrafts: StateFlow<List<SavedDraft>> = repository.savedDrafts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteSavedDraft(id: Int) {
        viewModelScope.launch {
            repository.deleteDraft(id)
        }
    }

    // Prepopulate chat from a law click
    fun consultAiAboutLaw(lawTitle: String) {
        selectedTab.value = 0 // Switch to Consultation tab
        chatInput.value = if (selectedLanguage.value == 1) {
            "क्या आप मुझे $lawTitle के प्रावधानों, प्रयोज्यता और पृष्ठभूमि को आसान शब्दों में समझा सकते हैं?"
        } else {
            "Can you explain the provisions, applicability, and historical background of the $lawTitle in simple terms?"
        }
    }
}

class LegalViewModelFactory(private val repository: LegalRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LegalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LegalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
