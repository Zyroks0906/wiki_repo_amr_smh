package com.amrsmh.wiki_repo_amr_smh.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator
import kotlinx.coroutines.launch

/**
 * Settings: conecta con PreferencesManager a través de ServiceLocator.
 * Muestra y permite cambiar las preferencias principales.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val prefs = ServiceLocator.providePreferencesManager()
    val scope = rememberCoroutineScope()

    val theme by prefs.themeFlow.collectAsState(initial = "SYSTEM")
    val order by prefs.listOrderFlow.collectAsState(initial = "BY_DATE")
    val favOnly by prefs.showFavoritesFlow.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Theme Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Current: $theme",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { scope.launch { prefs.setTheme("LIGHT") } },
                            modifier = Modifier.weight(1f),
                            colors = if (theme == "LIGHT") {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text("Light")
                        }
                        Button(
                            onClick = { scope.launch { prefs.setTheme("DARK") } },
                            modifier = Modifier.weight(1f),
                            colors = if (theme == "DARK") {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text("Dark")
                        }
                        Button(
                            onClick = { scope.launch { prefs.setTheme("SYSTEM") } },
                            modifier = Modifier.weight(1f),
                            colors = if (theme == "SYSTEM") {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text("System")
                        }
                    }
                }
            }

            // List Order Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "List Order",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Current: ${order.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { scope.launch { prefs.setListOrder("BY_DATE") } },
                            modifier = Modifier.weight(1f),
                            colors = if (order == "BY_DATE") {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text("By Date")
                        }
                        Button(
                            onClick = { scope.launch { prefs.setListOrder("BY_VALUE") } },
                            modifier = Modifier.weight(1f),
                            colors = if (order == "BY_VALUE") {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text("By Value")
                        }
                        Button(
                            onClick = { scope.launch { prefs.setListOrder("BY_RARITY") } },
                            modifier = Modifier.weight(1f),
                            colors = if (order == "BY_RARITY") {
                                ButtonDefaults.buttonColors()
                            } else {
                                ButtonDefaults.outlinedButtonColors()
                            }
                        ) {
                            Text("By Rarity")
                        }
                    }
                }
            }

            // Favorites Filter
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Show Only Favorites",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Filter list to show only favorite items",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = favOnly,
                        onCheckedChange = { scope.launch { prefs.setShowFavorites(it) } }
                    )
                }
            }
        }
    }
}