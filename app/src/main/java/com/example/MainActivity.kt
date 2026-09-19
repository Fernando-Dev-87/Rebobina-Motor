package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Architecture
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme

enum class AppScreen {
    PLACA,
    FERRAGEM,
    HISTORICO,
    FERRAMENTAS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var currentScreen by rememberSaveable { mutableStateOf(AppScreen.PLACA) }
                val rewindViewModel: MotorRewindViewModel = viewModel()
                val ferragemViewModel: FerragemViewModel = viewModel()

                BackHandler(enabled = currentScreen != AppScreen.PLACA) {
                    currentScreen = AppScreen.PLACA
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.testTag("main_bottom_navigation")
                        ) {
                            NavigationBarItem(
                                selected = currentScreen == AppScreen.PLACA,
                                onClick = { currentScreen = AppScreen.PLACA },
                                icon = { Icon(Icons.Default.Bolt, contentDescription = null) },
                                label = { Text("Cálculo") }
                            )

                            NavigationBarItem(
                                selected = currentScreen == AppScreen.FERRAGEM,
                                onClick = { currentScreen = AppScreen.FERRAGEM },
                                icon = { Icon(Icons.Default.Architecture, contentDescription = null) },
                                label = { Text("Ferragem") }
                            )

                            NavigationBarItem(
                                selected = currentScreen == AppScreen.HISTORICO,
                                onClick = { currentScreen = AppScreen.HISTORICO },
                                icon = { Icon(Icons.Default.History, contentDescription = null) },
                                label = { Text("Histórico") }
                            )

                            NavigationBarItem(
                                selected = currentScreen == AppScreen.FERRAMENTAS,
                                onClick = { currentScreen = AppScreen.FERRAMENTAS },
                                icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                                label = { Text("Oficina") }
                            )
                        }
                    }
                ) { innerPadding ->
                    val contentModifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    when (currentScreen) {
                        AppScreen.PLACA -> {
                            MotorRewindScreen(
                                viewModel = rewindViewModel,
                                onNavigateToFerragem = { currentScreen = AppScreen.FERRAGEM },
                                modifier = contentModifier
                            )
                        }
                        AppScreen.FERRAGEM -> {
                            FerragemMotorScreen(
                                viewModel = ferragemViewModel,
                                onNavigateToPlacaScreen = { currentScreen = AppScreen.PLACA },
                                modifier = contentModifier
                            )
                        }
                        AppScreen.HISTORICO -> {
                            HistoryScreen(modifier = contentModifier)
                        }
                        AppScreen.FERRAMENTAS -> {
                            ToolsScreen(modifier = contentModifier)
                        }
                    }
                }
            }
        }
    }
}
