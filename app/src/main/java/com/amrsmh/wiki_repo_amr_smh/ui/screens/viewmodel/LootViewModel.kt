package com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrsmh.wiki_repo_amr_smh.data.datastore.PreferencesManager
import com.amrsmh.wiki_repo_amr_smh.data.repository.LootRepository
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator
import com.amrsmh.wiki_repo_amr_smh.domain.models.LootItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LootUiState(
    val items: List<LootItem> = emptyList(),
    val isLoading: Boolean = false
)

class LootViewModel(
    private val repository: LootRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LootUiState())
    val uiState: StateFlow<LootUiState> = _uiState.asStateFlow()

    private val _isAddDialogOpen = MutableStateFlow(false)
    val isAddDialogOpen: StateFlow<Boolean> = _isAddDialogOpen.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeAll(),
                preferencesManager.showFavoritesFlow,
                preferencesManager.listOrderFlow
            ) { allItems, showFavoritesOnly, order ->
                var filtered = if (showFavoritesOnly) {
                    allItems.filter { it.isFavorite }
                } else {
                    allItems
                }

                // Ordenar segÃºn preferencia
                filtered = when (order) {
                    "BY_VALUE" -> filtered.sortedByDescending { it.value }
                    "BY_LOCATION" -> filtered.sortedBy { it.location }
                    else -> filtered.sortedByDescending { it.createdAt }
                }

                filtered
            }.collect { filteredItems ->
                _uiState.update { it.copy(items = filteredItems) }
            }
        }
    }

    fun showAddDialog(show: Boolean) {
        _isAddDialogOpen.value = show
    }

    fun addItem(item: LootItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(item)
        }
    }

    fun updateItem(item: LootItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(item)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }

    fun toggleFavorite(item: LootItem) {
        val updated = item.copy(isFavorite = !item.isFavorite)
        updateItem(updated)
    }

    fun observeById(id: Long): Flow<LootItem?> = repository.observeById(id)
}
