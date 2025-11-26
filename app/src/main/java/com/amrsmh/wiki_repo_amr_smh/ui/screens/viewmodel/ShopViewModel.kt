package com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.amrsmh.wiki_repo_amr_smh.data.repository.ShopItemRepository
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator
import com.amrsmh.wiki_repo_amr_smh.domain.models.ShopItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ShopUiState(
    val items: List<ShopItem> = emptyList(),
    val isLoading: Boolean = false
)

class ShopViewModel(private val repository: ShopItemRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ShopUiState())
    val uiState: StateFlow<ShopUiState> = _uiState.asStateFlow()

    private val _isAddDialogOpen = MutableStateFlow(false)
    val isAddDialogOpen: StateFlow<Boolean> = _isAddDialogOpen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collect { list ->
                _uiState.update { it.copy(items = list) }
            }
        }
    }

    fun showAddDialog(show: Boolean) {
        _isAddDialogOpen.value = show
    }

    fun addItem(item: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.add(item)
        }
    }

    fun updateItem(item: ShopItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(item)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(id)
        }
    }

    fun toggleFavorite(item: ShopItem) {
        val updated = item.copy(isFavorite = !item.isFavorite)
        updateItem(updated)
    }

    fun observeById(id: Long): Flow<ShopItem?> = repository.observeById(id)
}

@Suppress("UNCHECKED_CAST")
class ShopViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = ServiceLocator.provideShopItemRepository()
        return ShopViewModel(repo) as T
    }
}