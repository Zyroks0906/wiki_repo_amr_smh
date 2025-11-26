package com.amrsmh.wiki_repo_amr_smh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator
import com.amrsmh.wiki_repo_amr_smh.ui.navigation.RepoNavHost
import com.amrsmh.wiki_repo_amr_smh.ui.theme.RepoTheme

/**
 * MainActivity: inicializa ServiceLocator y muestra el NavHost
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inicializa DB, prefs, repo
        ServiceLocator.init(applicationContext)

        setContent {
            RepoTheme {
                RepoNavHost() // ArrancarÃ¡ en HomeRoute (Dashboard)
            }
        }
    }
}