package com.amrsmh.wiki_repo_amr_smh.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navigateBack: () -> Unit) {
    val prefs = ServiceLocator.providePreferencesManager()
    val scope = rememberCoroutineScope()

    val theme by prefs.themeFlow.collectAsState(initial = "SYSTEM")
    val order by prefs.listOrderFlow.collectAsState(initial = "BY_DATE")
    val favOnly by prefs.showFavoritesFlow.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tema",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Actual: ${when(theme) {
                            "LIGHT" -> "Claro"
                            "DARK" -> "Oscuro"
                            else -> "Sistema"
                        }}",
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
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        ) {
                            Text("Claro")
                        }
                        Button(
                            onClick = { scope.launch { prefs.setTheme("DARK") } },
                            modifier = Modifier.weight(1f),
                            colors = if (theme == "DARK") {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        ) {
                            Text("Oscuro")
                        }
                        Button(
                            onClick = { scope.launch { prefs.setTheme("SYSTEM") } },
                            modifier = Modifier.weight(1f),
                            colors = if (theme == "SYSTEM") {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        ) {
                            Text("Sistema")
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Orden de Lista",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Actual: ${when(order) {
                            "BY_DATE" -> "Por Fecha"
                            "BY_VALUE" -> "Por Valor"
                            "BY_PRICE" -> "Por Precio"
                            "BY_DANGER" -> "Por Peligro"
                            "BY_LOCATION" -> "Por Ubicación"
                            "BY_CATEGORY" -> "Por Categoría"
                            "BY_NAME" -> "Por Nombre"
                            else -> "Por Fecha"
                        }}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { scope.launch { prefs.setListOrder("BY_DATE") } },
                                modifier = Modifier.weight(1f),
                                colors = if (order == "BY_DATE") {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            ) {
                                Text("Por Fecha")
                            }
                            Button(
                                onClick = { scope.launch { prefs.setListOrder("BY_VALUE") } },
                                modifier = Modifier.weight(1f),
                                colors = if (order == "BY_VALUE") {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            ) {
                                Text("Por Valor")
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { scope.launch { prefs.setListOrder("BY_DANGER") } },
                                modifier = Modifier.weight(1f),
                                colors = if (order == "BY_DANGER") {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            ) {
                                Text("Por Peligro")
                            }
                            Button(
                                onClick = { scope.launch { prefs.setListOrder("BY_PRICE") } },
                                modifier = Modifier.weight(1f),
                                colors = if (order == "BY_PRICE") {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            ) {
                                Text("Por Precio")
                            }
                        }
                    }
                }
            }

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
                            text = "Mostrar Solo Favoritos",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Filtrar lista para mostrar solo objetos favoritos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = favOnly,
                        onCheckedChange = { scope.launch { prefs.setShowFavorites(it) } },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
        }
    }
}