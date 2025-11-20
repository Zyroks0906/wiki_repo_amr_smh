package com.amrsmh.wiki_repo_amr_smh.ui.screens.shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Shop screen — lista estática de items comprables.
 * Más tarde podéis cargar desde assets/shop.json o usar Room para wishlist.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen() {
    val shopItems = listOf(
        ShopItem("Stun Grenade", 50, "Throwable that temporarily stuns enemies"),
        ShopItem("Shockwave Grenade", 120, "Area-of-effect damage grenade"),
        ShopItem("Pocket C.A.R.T", 200, "Portable transportation device"),
        ShopItem("Energy Crystal", 30, "Power source for equipment"),
        ShopItem("Med Kit", 75, "Restores health to full"),
        ShopItem("Flashbang", 40, "Blinds enemies in radius"),
        ShopItem("Smoke Grenade", 35, "Creates smoke cover")
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shop") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shopItems) { item ->
                ShopItemCard(item = item)
            }
        }
    }
}

@Composable
private fun ShopItemCard(item: ShopItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "${item.price} SURPLUS",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = { /* TODO: add to wishlist */ }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add to wishlist"
                )
            }
        }
    }
}

// Data class para items de tienda
private data class ShopItem(
    val name: String,
    val price: Int,
    val description: String
)