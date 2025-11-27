package com.amrsmh.wiki_repo_amr_smh.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.amrsmh.wiki_repo_amr_smh.di.ServiceLocator

// Colores inspirados en el juego
private val DarkNavy = Color(0xFF0A1628)
private val DeepBlue = Color(0xFF162A47)
private val MidBlue = Color(0xFF1E3A5F)
private val BrightYellow = Color(0xFFFFD700)
private val AlertYellow = Color(0xFFFFA500)
private val DangerRed = Color(0xFFDC3545)
private val SuccessGreen = Color(0xFF28A745)
private val LightGray = Color(0xFFE0E0E0)
private val DarkGray = Color(0xFF2C3E50)

private val LightColors = lightColorScheme(
    primary = MidBlue,
    onPrimary = Color.White,
    primaryContainer = DeepBlue,
    onPrimaryContainer = BrightYellow,
    secondary = AlertYellow,
    onSecondary = DarkNavy,
    secondaryContainer = Color(0xFFFFE082),
    onSecondaryContainer = DarkNavy,
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    error = DangerRed,
    onError = Color.White,
    background = LightGray,
    onBackground = DarkNavy,
    surface = Color.White,
    onSurface = DarkNavy,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = DarkGray,
    outline = MidBlue
)

private val DarkColors = darkColorScheme(
    primary = MidBlue,
    onPrimary = BrightYellow,
    primaryContainer = DeepBlue,
    onPrimaryContainer = BrightYellow,
    secondary = BrightYellow,
    onSecondary = DarkNavy,
    secondaryContainer = AlertYellow,
    onSecondaryContainer = DarkNavy,
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    error = DangerRed,
    onError = Color.White,
    background = DarkNavy,
    onBackground = LightGray,
    surface = DeepBlue,
    onSurface = LightGray,
    surfaceVariant = MidBlue,
    onSurfaceVariant = LightGray,
    outline = BrightYellow
)

@Composable
fun RepoTheme(content: @Composable () -> Unit) {
    val prefsManager = ServiceLocator.providePreferencesManager()
    val themeMode by prefsManager.themeFlow.collectAsState(initial = "SYSTEM")
    val isSystemInDarkTheme = isSystemInDarkTheme()

    val useDark = when (themeMode) {
        "LIGHT" -> false
        "DARK" -> true
        else -> isSystemInDarkTheme
    }

    val colors = if (useDark) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}