package com.amrsmh.wiki_repo_amr_smh.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator

@Suppress("UNCHECKED_CAST")
class LootViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = ServiceLocator.provideLootRepository()
        val prefs = ServiceLocator.providePreferencesManager()
        return LootViewModel(repo, prefs) as T
    }
}