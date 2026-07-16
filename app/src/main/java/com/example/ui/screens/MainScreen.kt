package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.database.BookmarkedLaw
import com.example.data.database.ChatHistoryItem
import com.example.data.database.SavedDraft
import com.example.data.model.DraftTemplate
import com.example.data.model.DraftTemplates
import com.example.data.model.LawHubData
import com.example.data.model.LawItem
import com.example.ui.viewmodel.LegalViewModel

fun getLocalizedString(lang: Int, key: String): String {
    val english = mapOf(
        "tab_consult" to "Consult",
        "tab_drafting" to "Drafting",
        "tab_law_hub" to "Law Hub",
        "tab_vault" to "My Vault",
        "hero_title" to "How can I help you today?",
        "hero_desc" to "Ask me about constitutional acts, civil disputes, criminal procedures, or ask to analyze regulations.",
        "chat_placeholder" to "Ask a legal question... (e.g. RTI procedures)",
        "chat_clear" to "Clear Chat",
        "chat_send" to "Send",
        "chat_error" to "Error: ",
        "disclaimer_toast" to "Chat history cleared",
        "disclaimer_title" to "Disclaimer",
        "disclaimer_text" to "AI is an assistant, not a human lawyer.",
        "draft_header" to "AI Legal Drafter",
        "draft_desc" to "Choose a template below and provide details to generate an official legal draft.",
        "draft_select_template" to "Select Template",
        "draft_btn_generate" to "Generate Document Draft",
        "draft_btn_save" to "Save Document to Vault",
        "draft_btn_saved" to "Saved!",
        "draft_field_error" to "Please fill this field",
        "draft_output_title" to "Generated Draft Document",
        "law_hub_title" to "Public Law Library",
        "law_hub_desc" to "Browse, search, and bookmark essential public acts and constitutional rights offline.",
        "search_placeholder" to "Search acts, years, or descriptions...",
        "all_categories" to "All Categories",
        "bookmarked" to "Bookmarked",
        "unbookmarked" to "Add Bookmark",
        "consult_btn" to "Consult AI About This Law",
        "law_detail_title" to "Act Details & Applicability",
        "no_laws_found" to "No matching laws found in library.",
        "vault_title" to "My Vault",
        "vault_desc" to "Manage your generated documents and bookmarked acts.",
        "vault_saved_drafts" to "Saved Document Drafts",
        "vault_bookmarked_laws" to "Bookmarked Public Laws",
        "no_saved_drafts" to "No saved drafts yet. Generate and save legal documents in the Drafting tab.",
        "no_bookmarked_laws" to "No bookmarked laws yet. Browse and bookmark laws in the Law Hub tab.",
        "btn_delete" to "Delete",
        "btn_copy" to "Copy Text",
        "toast_copied" to "Copied to clipboard!",
        "btn_view_details" to "View Details"
    )

    val hindi = mapOf(
        "tab_consult" to "परामर्श",
        "tab_drafting" to "दस्तावेज़",
        "tab_law_hub" to "कानून हब",
        "tab_vault" to "मेरा वॉल्ट",
        "hero_title" to "आज मैं आपकी क्या सहायता कर सकता हूँ?",
        "hero_desc" to "संवैधानिक कानूनों, दीवानी मामलों, आपराधिक प्रक्रियाओं के बारे में पूछें, या नियमों को समझने में मदद लें।",
        "chat_placeholder" to "कानूनी प्रश्न पूछें... (उदा: RTI प्रक्रिया)",
        "chat_clear" to "चैट साफ़ करें",
        "chat_send" to "भेजें",
        "chat_error" to "त्रुटि: ",
        "disclaimer_toast" to "चैट इतिहास हटा दिया गया है",
        "disclaimer_title" to "अस्वीकरण",
        "disclaimer_text" to "एआई एक सहायक है, मानव वकील नहीं।",
        "draft_header" to "एआई कानूनी लेखक",
        "draft_desc" to "नीचे दिए गए प्रारूपों में से चुनें और आधिकारिक कानूनी दस्तावेज का मसौदा तैयार करने के लिए विवरण भरें।",
        "draft_select_template" to "प्रारूप (टैम्पलेट) चुनें",
        "draft_btn_generate" to "दस्तावेज़ ड्राफ्ट तैयार करें",
        "draft_btn_save" to "दस्तावेज़ को वॉल्ट में सहेजें",
        "draft_btn_saved" to "सहेज लिया गया!",
        "draft_field_error" to "कृपया इस फ़ील्ड को भरें",
        "draft_output_title" to "तैयार किया गया दस्तावेज़",
        "law_hub_title" to "सार्वजनिक कानून पुस्तकालय",
        "law_hub_desc" to "आवश्यक सार्वजनिक अधिनियमों और संवैधानिक अधिकारों को ऑफलाइन खोजें, पढ़ें और बुकमार्क करें।",
        "search_placeholder" to "अधिनियम, वर्ष या विवरण खोजें...",
        "all_categories" to "सभी श्रेणियां",
        "bookmarked" to "बुकमार्क किया गया",
        "unbookmarked" to "बुकमार्क जोड़ें",
        "consult_btn" to "इस कानून के बारे में एआई से पूछें",
        "law_detail_title" to "अधिनियम विवरण और प्रयोज्यता",
        "no_laws_found" to "पुस्तकालय में कोई मेल खाता कानून नहीं मिला।",
        "vault_title" to "मेरा वॉल्ट",
        "vault_desc" to "अपने तैयार किए गए दस्तावेज़ों और बुकमार्क किए गए कानूनों का प्रबंधन करें।",
        "vault_saved_drafts" to "सहेजे गए दस्तावेज़ ड्राफ्ट",
        "vault_bookmarked_laws" to "बुकमार्क किए गए सार्वजनिक कानून",
        "no_saved_drafts" to "अभी तक कोई सहेजे गए ड्राफ्ट नहीं हैं। 'दस्तावेज़' टैब में दस्तावेज़ तैयार करके सहेजें।",
        "no_bookmarked_laws" to "अभी तक कोई बुकमार्क कानून नहीं हैं। 'कानून हब' टैब में ब्राउज़ और बुकमार्क करें।",
        "btn_delete" to "हटाएं",
        "btn_copy" to "कॉपी करें",
        "toast_copied" to "क्लिपबोर्ड पर कॉपी किया गया!",
        "btn_view_details" to "विवरण देखें"
    )

    return if (lang == 1) hindi[key] ?: key else english[key] ?: key
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: LegalViewModel) {
    val selectedTab by viewModel.selectedTab
    val selectedLanguage by viewModel.selectedLanguage
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Gavel,
                            contentDescription = "Gavel Logo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = if (selectedLanguage == 1) "कानूनी सहायक AI" else "GovLegal AI",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
                actions = {
                    FilterChip(
                        selected = selectedLanguage == 1,
                        onClick = {
                            viewModel.selectedLanguage.value = if (selectedLanguage == 1) 0 else 1
                        },
                        label = {
                            Text(
                                text = if (selectedLanguage == 1) "हिन्दी" else "English",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Translate,
                                contentDescription = "Language",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .testTag("language_toggle_chip")
                    )

                    IconButton(
                        onClick = {
                            val msg = if (selectedLanguage == 0) {
                                "GovLegal Assistant v1.0.0 - Powered by Gemini"
                            } else {
                                "सरकारी कानूनी सहायक v1.0.0 - जेमिनी द्वारा संचालित"
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.Info, contentDescription = "App Info")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.selectedTab.value = 0 },
                    icon = { Icon(imageVector = Icons.Filled.Forum, contentDescription = "Consultation") },
                    label = { Text(getLocalizedString(selectedLanguage, "tab_consult")) },
                    modifier = Modifier.testTag("tab_consult")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.selectedTab.value = 1 },
                    icon = { Icon(imageVector = Icons.Filled.Description, contentDescription = "Draft Generator") },
                    label = { Text(getLocalizedString(selectedLanguage, "tab_drafting")) },
                    modifier = Modifier.testTag("tab_drafts")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.selectedTab.value = 2 },
                    icon = { Icon(imageVector = Icons.Filled.AutoStories, contentDescription = "Law Library") },
                    label = { Text(getLocalizedString(selectedLanguage, "tab_law_hub")) },
                    modifier = Modifier.testTag("tab_laws")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { viewModel.selectedTab.value = 3 },
                    icon = { Icon(imageVector = Icons.Filled.Folder, contentDescription = "Saved Items") },
                    label = { Text(getLocalizedString(selectedLanguage, "tab_vault")) },
                    modifier = Modifier.testTag("tab_vault")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (selectedTab) {
                0 -> ConsultTab(viewModel)
                1 -> DraftingTab(viewModel)
                2 -> LawHubTab(viewModel)
                3 -> VaultTab(viewModel)
            }
        }
    }
}

// ==========================================
// CONSULTATION TAB (CHATBOT)
// ==========================================
@Composable
fun ConsultTab(viewModel: LegalViewModel) {
    val chatHistory by viewModel.chatHistory.collectAsStateWithLifecycle()
    val isLoading by viewModel.isChatLoading
    val input by viewModel.chatInput
    val error by viewModel.chatError
    val selectedLanguage by viewModel.selectedLanguage

    Column(modifier = Modifier.fillMaxSize()) {
        // Simple Hero Banner or introduction
        if (chatHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_legal_hero),
                    contentDescription = "Legal Hero Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (selectedLanguage == 1) "विशेषज्ञ कानूनी परामर्श" else "Expert Legal Consultation",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = if (selectedLanguage == 1) "अधिकारों, नियमों और नीतियों के बारे में प्रश्न पूछें।" else "Ask questions regarding rights, compliance, and policies.",
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (chatHistory.isEmpty()) {
                // Empty state quick prompts
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = "AI Suggestions",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = getLocalizedString(selectedLanguage, "hero_title"),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (selectedLanguage == 1) "नीचे दिए गए किसी विषय को चुनें या अपना कानूनी प्रश्न लिखें।" else "Select a suggested topic below or write your legal query.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    val suggestions = if (selectedLanguage == 1) listOf(
                        "RTI अनुरोध दायर करने की क्या प्रक्रिया है?",
                        "उपभोक्ता संरक्षण अधिनियम के तहत मेरे बुनियादी अधिकार क्या हैं?",
                        "मुझे नागरिक के प्रमुख मौलिक अधिकारों के बारे में बताएं।",
                        "निजी कंपनियों के लिए मातृत्व अवकाश की क्या नीतियां हैं?"
                    ) else listOf(
                        "What is the procedure to file an RTI request?",
                        "What are my basic rights under the Consumer Protection Act?",
                        "Tell me about the key fundamental rights of a citizen.",
                        "What are the mandatory maternity leave policies for private firms?"
                    )

                    suggestions.forEach { suggestion ->
                        Card(
                            onClick = { viewModel.chatInput.value = suggestion },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.HelpOutline,
                                    contentDescription = "Suggestion item",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = suggestion,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            } else {
                // Scrollable Chat Items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(chatHistory) { item ->
                        ChatBubble(item)
                    }

                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (selectedLanguage == 1) "एआई सहायक परामर्श तैयार कर रहा है..." else "AI assistant is preparing consultation...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Error message banner
        if (error != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "Error icon",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = error ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Bottom Input Row
        Surface(
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .imePadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chatHistory.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.clearChat() },
                        modifier = Modifier.testTag("chat_clear_button"),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Clear Chat")
                    }
                }

                TextField(
                    value = input,
                    onValueChange = { viewModel.chatInput.value = it },
                    placeholder = { Text(getLocalizedString(selectedLanguage, "chat_placeholder")) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    maxLines = 4,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(onSend = { viewModel.sendChatMessage() })
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = { viewModel.sendChatMessage() },
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("chat_send_button"),
                    shape = RoundedCornerShape(24.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Send Message",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(item: ChatHistoryItem) {
    val isUser = item.role == "user"
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val bg = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val color = if (isUser) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
    val shape = if (isUser) {
        RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp)
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = alignment
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!isUser) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .align(Alignment.Top),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Gavel,
                        contentDescription = "AI icon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Surface(
                color = bg,
                contentColor = color,
                shape = shape,
                modifier = Modifier.widthIn(max = 280.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = item.message,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }

            if (isUser) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .align(Alignment.Top),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "User icon",
                        tint = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

fun getLocalizedTemplateTitle(lang: Int, title: String): String {
    if (lang == 1) {
        return when (title) {
            "Lease Agreement" -> "किराया समझौता (Lease Agreement)"
            "RTI Application" -> "RTI आवेदन (RTI Application)"
            "Affidavit of Name Change" -> "नाम बदलने का शपथ पत्र (Affidavit)"
            "Consumer Complaint Notice" -> "उपभोक्ता शिकायत नोटिस"
            else -> title
        }
    }
    return title
}

fun getLocalizedTemplateCategory(lang: Int, category: String): String {
    if (lang == 1) {
        return when (category) {
            "Property Law" -> "संपत्ति कानून"
            "Transparency" -> "पारदर्शिता"
            "Civil Law" -> "दीवानी कानून"
            "Consumer Rights" -> "उपभोक्ता अधिकार"
            else -> category
        }
    }
    return category
}

fun getLocalizedFieldLabel(lang: Int, label: String): String {
    if (lang == 1) {
        return when (label) {
            "Landlord Full Name" -> "मकान मालिक का पूरा नाम"
            "Tenant Full Name" -> "किराएदार का पूरा नाम"
            "Property Address" -> "संपत्ति का पता"
            "Monthly Rent (INR)" -> "मासिक किराया (रुपये)"
            "Security Deposit (INR)" -> "सुरक्षा जमा राशि (रुपये)"
            "Agreement Duration (Months)" -> "समझौते की अवधि (महीने)"
            "Public Authority Department" -> "लोक प्राधिकरण/सरकारी विभाग का नाम"
            "Information Required Description" -> "आवश्यक जानकारी का विवरण"
            "Period for which Info is needed" -> "वह समय अवधि जिसके लिए जानकारी चाहिए"
            "Applicant Contact Address" -> "आवेदक का संपर्क पता"
            "Old Full Name" -> "पुराना पूरा नाम"
            "New Full Name" -> "नया पूरा नाम"
            "Father's/Spouse Name" -> "पिता या जीवनसाथी का नाम"
            "Reason for Change" -> "नाम बदलने का मुख्य कारण"
            "Applicant Residential Address" -> "आवेदक का आवासीय पता"
            "Opposite Party Name (Company/Vendor)" -> "विपक्षी पार्टी का नाम (कंपनी/विक्रेता)"
            "Product or Service Name" -> "उत्पाद या सेवा का नाम"
            "Date of Purchase / Incident" -> "खरीद या घटना की तारीख"
            "Amount Claimed (INR)" -> "दावा की गई राशि (रुपये)"
            "Defect or Grievance Description" -> "दोष या शिकायत का संक्षिप्त विवरण"
            else -> label
        }
    }
    return label
}

// ==========================================
// DRAFTING TAB
// ==========================================
@Composable
fun DraftingTab(viewModel: LegalViewModel) {
    val selectedTemplate by viewModel.selectedTemplate
    val fields = viewModel.draftFields
    val generatedDraft by viewModel.generatedDraft
    val isLoading by viewModel.isDraftLoading
    val error by viewModel.draftError
    val selectedLanguage by viewModel.selectedLanguage
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(androidx.compose.foundation.rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = getLocalizedString(selectedLanguage, "draft_header"),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = getLocalizedString(selectedLanguage, "draft_desc"),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Horizontal Template Selector
        Text(
            text = getLocalizedString(selectedLanguage, "draft_select_template") + ":",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(DraftTemplates.templates) { template ->
                val isSelected = selectedTemplate.id == template.id
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectTemplate(template) },
                    label = { Text(getLocalizedTemplateTitle(selectedLanguage, template.title)) },
                    leadingIcon = if (isSelected) {
                        { Icon(Icons.Filled.Check, "Selected") }
                    } else null
                )
            }
        }

        // Active template description
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = if (selectedLanguage == 1) {
                        "श्रेणी: ${getLocalizedTemplateCategory(selectedLanguage, selectedTemplate.category)}"
                    } else {
                        "Category: ${selectedTemplate.category}"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (selectedLanguage == 1) {
                        // Hindi description of template
                        when (selectedTemplate.title) {
                            "Lease Agreement" -> "मकान मालिक और किराएदार के बीच नियम और शर्तें स्थापित करने के लिए औपचारिक किराया अनुबंध समझौता।"
                            "RTI Application" -> "सरकारी विभाग या लोक प्राधिकरण से आधिकारिक जानकारी या रिकॉर्ड प्राप्त करने के लिए आरटीआई (सूचना का अधिकार) आवेदन।"
                            "Affidavit of Name Change" -> "नाम बदलने की घोषणा करने और उसे कानूनी दस्तावेजों में अपडेट करने के लिए प्रथम श्रेणी का शपथ पत्र।"
                            "Consumer Complaint Notice" -> "त्रुटिपूर्ण उत्पाद, दोषपूर्ण सेवा या अनुचित व्यापार प्रथाओं के खिलाफ विक्रेता या सेवा प्रदाता को कानूनी नोटिस।"
                            else -> selectedTemplate.description
                        }
                    } else {
                        selectedTemplate.description
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Input Form Fields
        Text(
            text = if (selectedLanguage == 1) "दस्तावेज़ की जानकारी दर्ज करें:" else "Enter Document Information:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        selectedTemplate.fields.forEach { field ->
            val value = fields[field.key] ?: ""
            OutlinedTextField(
                value = value,
                onValueChange = { fields[field.key] = it },
                label = { Text(getLocalizedFieldLabel(selectedLanguage, field.label)) },
                placeholder = {
                    Text(
                        if (selectedLanguage == 1) {
                            when (field.placeholder) {
                                "e.g. John Doe" -> "उदा. रमेश कुमार"
                                "e.g. Jane Doe" -> "उदा. सुनीता शर्मा"
                                "e.g. Flat 101, Shanti Apartments, Delhi" -> "उदा. फ्लैट 101, शांति अपार्टमेंट, दिल्ली"
                                "e.g. 15000" -> "उदा. 15000"
                                "e.g. 30000" -> "उदा. 30000"
                                "e.g. 11" -> "उदा. 11"
                                "e.g. Municipal Corporation, Zone 5" -> "उदा. नगर निगम, ज़ोन 5"
                                "e.g. Copies of approved building plans for Plot X" -> "उदा. प्लॉट एक्स के स्वीकृत भवन योजनाओं की प्रतियां"
                                "e.g. January 2024 to December 2024" -> "उदा. जनवरी 2024 से दिसंबर 2024"
                                "e.g. Shanti Nagar, Delhi" -> "उदा. शांति नगर, दिल्ली"
                                "e.g. Ramesh Chandra" -> "उदा. रमेश चंद्र"
                                "e.g. Error in spelling in original school record" -> "उदा. स्कूल रिकॉर्ड में हिज्जे/वर्तनी की गलती"
                                "e.g. ABC Electronics Ltd" -> "उदा. एबीसी इलेक्ट्रॉनिक्स लिमिटेड"
                                "e.g. Smart LED TV 43 inch" -> "उदा. स्मार्ट एलईडी टीवी 43 इंच"
                                "e.g. 2024-05-12" -> "उदा. 2024-05-12"
                                "e.g. 25000" -> "उदा. 25000"
                                "e.g. Screen stopped working within warranty, company refused repair" -> "उदा. वारंटी के भीतर स्क्रीन ने काम करना बंद कर दिया, कंपनी ने मरम्मत से इनकार कर दिया"
                                else -> field.placeholder
                            }
                        } else {
                            field.placeholder
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                )
            )
        }

        // Generate Button
        Button(
            onClick = { viewModel.generateLegalDraft() },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("draft_generate_button"),
            enabled = !isLoading && fields.values.any { it.isNotBlank() }
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (selectedLanguage == 1) "जेमिनी द्वारा मसौदा तैयार किया जा रहा है..." else "Drafting with Gemini...")
            } else {
                Icon(imageVector = Icons.Filled.AutoAwesome, contentDescription = "Draft Icon")
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (selectedLanguage == 1) "आधिकारिक कानूनी दस्तावेज़ ड्राफ्ट करें" else "Generate Custom Legal Draft")
            }
        }

        // Error message if any
        error?.let { err ->
            Text(
                text = err,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Display Generated Draft inside a Card
        generatedDraft?.let { draft ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("generated_draft_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getLocalizedString(selectedLanguage, "draft_output_title") + ":",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(draft))
                                    Toast.makeText(
                                        context,
                                        getLocalizedString(selectedLanguage, "toast_copied"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.testTag("draft_copy_button")
                            ) {
                                Icon(Icons.Filled.ContentCopy, "Copy")
                            }
                            IconButton(
                                onClick = {
                                    viewModel.saveCurrentDraft()
                                    Toast.makeText(
                                        context,
                                        if (selectedLanguage == 1) "दस्तावेज़ सफलतापूर्वक वॉल्ट में सहेज लिया गया है!" else "Draft saved offline in My Vault!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.testTag("draft_save_button")
                            ) {
                                Icon(Icons.Filled.Save, "Save")
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    Text(
                        text = draft,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .verticalScroll(androidx.compose.foundation.rememberScrollState())
                    )
                }
            }
        }
    }
}

// ==========================================
@Composable
fun LawHubTab(viewModel: LegalViewModel) {
    val query by viewModel.searchQuery
    val activeCategory by viewModel.selectedLawCategory
    val bookmarkedTitles by viewModel.bookmarkedTitles.collectAsStateWithLifecycle()
    var selectedLawForDialog by remember { mutableStateOf<LawItem?>(null) }
    val selectedLanguage by viewModel.selectedLanguage

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = getLocalizedString(selectedLanguage, "law_hub_title"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Search Bar
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text(getLocalizedString(selectedLanguage, "search_placeholder")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("law_search_input"),
                leadingIcon = { Icon(Icons.Filled.Search, "Search") },
                trailingIcon = if (query.isNotEmpty()) {
                    {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Filled.Clear, "Clear")
                        }
                    }
                } else null,
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            // Category Horizontal Chips
            val categoriesEn = listOf("All", "Transparency", "Consumer Rights", "Constitutional Law", "Civil Law", "Criminal Law", "Labor Law", "Environmental Law")
            val categoriesHi = listOf("सभी", "पारदर्शिता", "उपभोक्ता अधिकार", "संवैधानिक कानून", "दीवानी कानून", "आपराधिक कानून", "श्रम कानून", "पर्यावरण कानून")
            val categories = if (selectedLanguage == 1) categoriesHi else categoriesEn

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val index = categories.indexOf(category)
                    val correspondingEn = categoriesEn[index]
                    val isSelected = activeCategory == correspondingEn
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectedLawCategory.value = correspondingEn },
                        label = { Text(category) }
                    )
                }
            }
        }

        // Filter items
        val filteredLaws = LawHubData.items.filter { item ->
            val matchesCategory = activeCategory == "All" || item.category == activeCategory
            val matchesSearch = if (selectedLanguage == 1) {
                item.titleHi.contains(query, ignoreCase = true) ||
                        item.descriptionHi.contains(query, ignoreCase = true) ||
                        item.categoryHi.contains(query, ignoreCase = true)
            } else {
                item.title.contains(query, ignoreCase = true) ||
                        item.description.contains(query, ignoreCase = true) ||
                        item.category.contains(query, ignoreCase = true)
            }
            matchesCategory && matchesSearch
        }

        if (filteredLaws.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.SearchOff,
                        contentDescription = "No results",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = getLocalizedString(selectedLanguage, "no_laws_found"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredLaws) { law ->
                    val isBookmarked = bookmarkedTitles.contains(law.title)
                    val displayCategory = if (selectedLanguage == 1) law.categoryHi else law.category
                    val displayTitle = if (selectedLanguage == 1) law.titleHi else law.title
                    val displayDesc = if (selectedLanguage == 1) law.descriptionHi else law.description

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedLawForDialog = law }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (selectedLanguage == 1) "$displayCategory • ${law.actYear} का अधिनियम" else "$displayCategory • Act of ${law.actYear}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = displayTitle,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.toggleBookmarkLaw(
                                            law.title,
                                            law.description,
                                            law.category,
                                            law.actYear
                                        )
                                    },
                                    modifier = Modifier.testTag("law_bookmark_button")
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = "Bookmark Law",
                                        tint = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = displayDesc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { viewModel.consultAiAboutLaw(law.title) },
                                    modifier = Modifier.testTag("law_consult_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Forum,
                                        contentDescription = "Consult icon",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (selectedLanguage == 1) "एआई परामर्श" else "Consult AI")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = { selectedLawForDialog = law }) {
                                    Text(if (selectedLanguage == 1) "विवरण पढ़ें" else "Read Summary")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog
    selectedLawForDialog?.let { law ->
        Dialog(onDismissRequest = { selectedLawForDialog = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedLanguage == 1) "संदर्भ मार्गदर्शिका" else "Reference Guide",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { selectedLawForDialog = null }) {
                            Icon(Icons.Filled.Close, "Close")
                        }
                    }

                    Text(
                        text = if (selectedLanguage == 1) law.titleHi else law.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text(if (selectedLanguage == 1) law.categoryHi else law.category) }
                        )
                        SuggestionChip(
                            onClick = {},
                            label = { Text(if (selectedLanguage == 1) "वर्ष: ${law.actYear}" else "Year: ${law.actYear}") }
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (selectedLanguage == 1) "अवलोकन:" else "Overview:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = if (selectedLanguage == 1) law.descriptionHi else law.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (selectedLanguage == 1) "मुख्य प्रावधान और नियम:" else "Core Provisions:",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = if (selectedLanguage == 1) law.detailsHi else law.details,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.consultAiAboutLaw(law.title)
                            selectedLawForDialog = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.Forum, "Consult AI")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (selectedLanguage == 1) "एआई सलाहकार के साथ विश्लेषण करें" else "Analyze With AI Consultant")
                    }
                }
            }
        }
    }
}

// ==========================================
// MY VAULT TAB (SAVED ITEMS & HISTORY)
// ==========================================
@Composable
fun VaultTab(viewModel: LegalViewModel) {
    val savedDrafts by viewModel.savedDrafts.collectAsStateWithLifecycle()
    val bookmarkedLaws by viewModel.bookmarkedLaws.collectAsStateWithLifecycle()
    var selectedDraftForDialog by remember { mutableStateOf<SavedDraft?>(null) }
    var selectedVaultTab by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val selectedLanguage by viewModel.selectedLanguage

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (selectedLanguage == 1) "मेरा व्यक्तिगत कानूनी वॉल्ट" else "My Personal Legal Vault",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (selectedLanguage == 1) "अपने सहेजे गए कानूनी ड्राफ्ट, दस्तावेज़ और बुकमार्क किए गए नियमों को पूरी तरह से ऑफ़लाइन एक्सेस करें।" else "Access your saved legal drafts, documents, and bookmarked acts completely offline.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            TabRow(selectedTabIndex = selectedVaultTab) {
                Tab(
                    selected = selectedVaultTab == 0,
                    onClick = { selectedVaultTab = 0 },
                    text = { Text(if (selectedLanguage == 1) "सहेजे गए ड्राफ्ट (${savedDrafts.size})" else "Saved Drafts (${savedDrafts.size})") }
                )
                Tab(
                    selected = selectedVaultTab == 1,
                    onClick = { selectedVaultTab = 1 },
                    text = { Text(if (selectedLanguage == 1) "बुकमार्क (${bookmarkedLaws.size})" else "Bookmarks (${bookmarkedLaws.size})") }
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (selectedVaultTab == 0) {
                // SAVED DRAFTS
                if (savedDrafts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Description,
                                contentDescription = "No drafts",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (selectedLanguage == 1) "आपका कानूनी वॉल्ट खाली है।" else "Your Legal Vault is empty.",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (selectedLanguage == 1) "आधिकारिक कानूनी टेम्पलेट बनाने, स्वरूपित करने और ऑफ़लाइन सहेजने के लिए 'ड्राफ्टिंग' टैब पर जाएं।" else "Go to the 'Drafting' tab to create, format, and save official legal templates offline.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(savedDrafts) { draft ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { selectedDraftForDialog = draft }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = getLocalizedTemplateCategory(selectedLanguage, draft.category),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = getLocalizedTemplateTitle(selectedLanguage, draft.title),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = if (selectedLanguage == 1) {
                                                "सृजित: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(draft.timestamp))}"
                                            } else {
                                                "Created: ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(draft.timestamp))}"
                                            },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Row {
                                        IconButton(
                                            onClick = {
                                                clipboardManager.setText(AnnotatedString(draft.content))
                                                Toast.makeText(
                                                    context,
                                                    getLocalizedString(selectedLanguage, "toast_copied"),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        ) {
                                            Icon(Icons.Filled.ContentCopy, if (selectedLanguage == 1) "कॉपी करें" else "Copy")
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteSavedDraft(draft.id) }
                                        ) {
                                            Icon(Icons.Filled.Delete, "Delete", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // BOOKMARKED LAWS
                if (bookmarkedLaws.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.BookmarkBorder,
                                contentDescription = "No bookmarks",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (selectedLanguage == 1) "कोई बुकमार्क किया गया कानून नहीं है।" else "No bookmarked laws.",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (selectedLanguage == 1) "यहां तुरंत एक्सेस करने के लिए 'लॉ हब' से आवश्यक कानूनों और अधिनियमों को बुकमार्क करें।" else "Bookmark essential laws and acts from the 'Law Hub' to access them instantly here.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(bookmarkedLaws) { law ->
                            val originalLaw = LawHubData.items.find { it.title == law.title }
                            val displayCategory = if (selectedLanguage == 1 && originalLaw != null) originalLaw.categoryHi else law.category
                            val displayTitle = if (selectedLanguage == 1 && originalLaw != null) originalLaw.titleHi else law.title
                            val displayDesc = if (selectedLanguage == 1 && originalLaw != null) originalLaw.descriptionHi else law.description

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (selectedLanguage == 1) "$displayCategory • ${law.actYear} का अधिनियम" else "${law.category} • Act of ${law.actYear}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = displayTitle,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                viewModel.toggleBookmarkLaw(
                                                    law.title,
                                                    law.description,
                                                    law.category,
                                                    law.actYear
                                                )
                                            }
                                        ) {
                                            Icon(Icons.Filled.Bookmark, if (selectedLanguage == 1) "बुकमार्क हटाएं" else "Unbookmark", tint = MaterialTheme.colorScheme.primary)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = displayDesc,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        TextButton(onClick = { viewModel.consultAiAboutLaw(law.title) }) {
                                            Icon(Icons.Filled.Forum, "Consult AI", modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(if (selectedLanguage == 1) "एआई परामर्श" else "Consult AI")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Saved Draft Viewer Dialog
    selectedDraftForDialog?.let { draft ->
        Dialog(onDismissRequest = { selectedDraftForDialog = null }) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = getLocalizedTemplateCategory(selectedLanguage, draft.category),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = getLocalizedTemplateTitle(selectedLanguage, draft.title),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(draft.content))
                                    Toast.makeText(
                                        context,
                                        getLocalizedString(selectedLanguage, "toast_copied"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                Icon(Icons.Filled.ContentCopy, "Copy")
                            }

                            IconButton(onClick = { selectedDraftForDialog = null }) {
                                Icon(Icons.Filled.Close, "Close")
                            }
                        }
                    }

                    HorizontalDivider()

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .heightIn(max = 350.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Box(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = draft.content,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = { selectedDraftForDialog = null }) {
                            Text(if (selectedLanguage == 1) "पूर्ण" else "Done")
                        }
                    }
                }
            }
        }
    }
}
