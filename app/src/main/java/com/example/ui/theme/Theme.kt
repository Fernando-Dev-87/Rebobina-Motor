package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CobaltBlueLight,
    onPrimary = Color.White,
    primaryContainer = CobaltBlue,
    onPrimaryContainer = Color.White,
    secondary = AmberCopper,
    onSecondary = Color.Black,
    secondaryContainer = GraphiteSurface,
    onSecondaryContainer = AmberCopper,
    tertiary = IndustrialSuccess,
    background = GraphiteDark,
    onBackground = TextPrimaryDark,
    surface = GraphiteSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = SteelBorder,
    onSurfaceVariant = TextSecondaryDark,
    error = IndustrialCritical,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CobaltBlue,
    onPrimary = Color.White,
    primaryContainer = CobaltBlueLight.copy(alpha = 0.1f),
    onPrimaryContainer = CobaltBlueDark,
    secondary = AmberCopperDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF92400E),
    tertiary = IndustrialSuccess,
    background = Color(0xFFF9FAFB),
    onBackground = TextPrimaryLight,
    surface = Color.White,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = TextSecondaryLight,
    error = IndustrialCritical,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
