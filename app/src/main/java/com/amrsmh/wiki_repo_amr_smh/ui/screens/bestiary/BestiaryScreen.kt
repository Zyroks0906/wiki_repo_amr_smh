package com.amrsmh.wiki_repo_amr_smh.ui.screens.bestiary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Bestiary: contenido estático (podéis reemplazar por JSON desde assets más tarde).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestiaryScreen() {
    val sampleMonsters = listOf(
        Monster(
            name = "Huntsman",
            danger = 3,
            detection = "Detects by sound",
            notes = "Stay quiet, use thrown decoys to distract"
        ),
        Monster(
            name = "Hidden",
            danger = 3,
            detection = "Stealth grabber",
            notes = "Check corners carefully, listen for breathing sounds"
        ),
        Monster(
            name = "Animal",
            danger = 2,
            detection = "Aggressive, affected by thrown objects",
            notes = "Can be distracted with throwables, avoid direct confrontation"
        ),
        Monster(
            name = "Screamer",
            danger = 4,
            detection = "Sound-based alert",
            notes = "Kill quickly before it alerts others, prioritize headshots"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Bestiary") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleMonsters) { monster ->
                MonsterCard(monster = monster)
            }
        }
    }
}

@Composable
private fun MonsterCard(monster: Monster) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = monster.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Surface(
                    color = when (monster.danger) {
                        in 1..2 -> MaterialTheme.colorScheme.tertiary
                        3 -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.error
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Danger: ${monster.danger}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Detection
            Text(
                text = "Detection: ${monster.detection}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            // Tactical Notes
            Text(
                text = "Tactical notes:",
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = monster.notes,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data class para los monstruos
private data class Monster(
    val name: String,
    val danger: Int,
    val detection: String,
    val notes: String
)