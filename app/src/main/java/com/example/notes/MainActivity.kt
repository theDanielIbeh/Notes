package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.notes.screens.util.FileUtils.DOCUMENTS
import com.example.notes.screens.util.FileUtils.PICTURES
import com.example.notes.screens.util.FileUtils.createFilesDirectory
import com.example.notes.ui.theme.NotesTheme
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                NotesTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NotesMain(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
        /*Create Directories*/
        createFilesDirectory(application, PICTURES)
        createFilesDirectory(application, DOCUMENTS)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@PreviewLightDark
@Composable
fun NotesPreview() {
    NotesTheme {
        Greeting("Android")
    }
}