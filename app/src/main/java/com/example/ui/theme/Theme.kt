package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = MiaPurpleLight,
    onPrimary = Color(0xFF1E0B4B),
    primaryContainer = Color(0xFF42278C),
    onPrimaryContainer = Color(0xFFEDE7FF),
    secondary = MiaPinkLight,
    onSecondary = Color(0xFF4A0020),
    secondaryContainer = Color(0xFF6B1039),
    onSecondaryContainer = Color(0xFFFFD9E6),
    tertiary = MiaGold,
    onTertiary = Color(0xFF332200),
    tertiaryContainer = Color(0xFF4F3800),
    onTertiaryContainer = Color(0xFFFFECB3),
    background = MiaBackgroundDark,
    onBackground = MiaOnBackgroundDark,
    surface = MiaSurfaceDark,
    onSurface = MiaOnBackgroundDark,
    surfaceVariant = MiaSurfaceVariantDark,
    onSurfaceVariant = MiaOnSurfaceVariantDark,
    outline = MiaOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = MiaPurple,
    onPrimary = Color.White,
    primaryContainer = MiaPurpleContainer,
    onPrimaryContainer = Color(0xFF280E6B),
    secondary = MiaPink,
    onSecondary = Color.White,
    secondaryContainer = MiaPinkContainer,
    onSecondaryContainer = Color(0xFF5E0028),
    tertiary = MiaGold,
    onTertiary = Color(0xFF2B1C00),
    tertiaryContainer = MiaGoldContainer,
    onTertiaryContainer = Color(0xFF402B00),
    background = MiaBackgroundLight,
    onBackground = MiaOnBackgroundLight,
    surface = MiaSurfaceLight,
    onSurface = MiaOnBackgroundLight,
    surfaceVariant = MiaSurfaceVariantLight,
    onSurfaceVariant = MiaOnSurfaceVariantLight,
    outline = MiaOutlineLight
)

val MiaShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun MiaTalksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = MiaShapes,
        content = content
    )
}
