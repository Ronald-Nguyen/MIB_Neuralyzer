package com.example.mibneuralyzer

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mibneuralyzer.ui.theme.MIBNeuralyzerTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private var isFlashing by mutableStateOf(false)
    private var originalBrightness: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Hole die aktuelle Bildschirmhelligkeit
        originalBrightness = getCurrentBrightness()

        setContent {
            MIBNeuralyzerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backgroundColor = if (isFlashing) Color.White else Color.Black

                    // LaunchedEffect um den Bildschirm aufblitzen zu lassen
                    LaunchedEffect(isFlashing) {
                        if (isFlashing) {
                            setBrightness(255) // Setze die Helligkeit auf Maximum
                            delay(200) // Bildschirm für 200ms aufblitzen
                        } else {
                            setBrightness(originalBrightness) // Setze die Helligkeit zurück auf den ursprünglichen Wert
                        }
                    }

                    // Layout mit Hintergrundfarbe
                    // Neuralyzer-Bild mit Opazität basierend auf dem Blitzstatus
                    Image(
                        painter = painterResource(id = R.drawable.images), // Bild laden
                        contentDescription = "Neuralyzer",
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .background(backgroundColor)
                            .graphicsLayer(alpha = if (isFlashing) 0f else 1f) // Setze die Opazität
                    )
                }
            }
        }
    }

    private fun getCurrentBrightness(): Int {
        return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, 128) // Standardwert 128 (50%)
    }

    private fun setBrightness(value: Int) {
        val resolver: ContentResolver = contentResolver
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, value)
    }

    private fun openBrightnessSettings() {
        val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
        startActivity(intent)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            isFlashing = true // Setze aufblitzen auf true
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            isFlashing = false // Setze aufblitzen auf false
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MIBNeuralyzerTheme {
        // Vorschau für das Neuralyzer-Bild
        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Neuralyzer"
        )
    }
}
