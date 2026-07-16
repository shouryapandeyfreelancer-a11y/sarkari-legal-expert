package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.data.database.AppDatabase
import com.example.data.repository.LegalRepository
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.LegalViewModel
import com.example.ui.viewmodel.LegalViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Local Database & Repository
        val database = AppDatabase.getDatabase(this)
        val repository = LegalRepository(
            chatDao = database.chatHistoryDao(),
            draftDao = database.savedDraftDao(),
            bookmarkDao = database.bookmarkedLawDao()
        )

        // Instantiate LegalViewModel
        val factory = LegalViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[LegalViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
