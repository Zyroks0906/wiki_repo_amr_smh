package com.amrsmh.wiki_repo_amr_smh.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFBF3B3B),
    onPrimary = Color.White,
    secondary = Color(0xFF6B6B6B),
    background = Color(0xFFF3EFE6),
    surface = Color.White,
    onBackground = Color(0xFF121212),
    onSurface = Color(0xFF121212)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFEF6C6C),
    onPrimary = Color.Black,
    secondary = Color(0xFF9E9E9E),
    background = Color(0xFF0F0F0F),
    surface = Color(0xFF121212),
    onBackground = Color(0xFFECECEC),
    onSurface = Color(0xFFECECEC)
)

@Composable
fun RepoTheme(useDark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (useDark) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
