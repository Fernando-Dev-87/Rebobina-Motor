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
    primary = CopperAmber,
    onPrimary = Color(0xFF1E1B18),
    primaryContainer = Color(0xFF78350F),
    onPrimaryContainer = Color(0xFFFEF3C7),
    secondary = ElectricCyanLight,
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF075985),
    onSecondaryContainer = Color(0xFFE0F2FE),
    tertiary = IndustrialSuccess,
    background = IndustrialDarkBg,
    onBackground = TextPrimaryDark,
    surface = IndustrialDarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = IndustrialDarkSurfaceVariant,
    onSurfaceVariant = TextSecondaryDark,
    error = IndustrialCritical,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CopperAmberDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = ElectricCyan,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = IndustrialSuccess,
    background = IndustrialLightBg,
    onBackground = TextPrimaryLight,
    surface = IndustrialLightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = IndustrialLightSurfaceVariant,
    onSurfaceVariant = TextSecondaryLight,
    error = IndustrialCritical,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Para chão de fábrica, mantemos o contraste da paleta industrial industrialmente calibrada
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
