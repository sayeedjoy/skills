@file:OptIn(
    androidx.compose.ui.text.ExperimentalTextApi::class,
    androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class
)

package com.example.app.ui.theme // TODO: change to the project's package

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.example.app.R // TODO: change to the project's R

/** User-selectable theme mode. Persist it in DataStore/prefs from the ViewModel layer. */
enum class ThemeMode { System, Light, Dark }

// Rounded variable font. One file, several weight instances, ROND=100 for soft terminals.
private fun roundedFont(weight: Int) = Font(
    resId = R.font.google_sans_flex,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(
        FontVariation.weight(weight),
        FontVariation.width(100f),
        FontVariation.Setting("ROND", 100f)
    )
)

val AppFontFamily = FontFamily(
    roundedFont(400),
    roundedFont(500),
    roundedFont(600),
    roundedFont(700),
)

private fun TextStyle.rounded() = copy(fontFamily = AppFontFamily)

private val AppTypography: Typography = Typography().let { base ->
    Typography(
        displayLarge = base.displayLarge.rounded(),
        displayMedium = base.displayMedium.rounded(),
        displaySmall = base.displaySmall.rounded(),
        headlineLarge = base.headlineLarge.rounded(),
        headlineMedium = base.headlineMedium.rounded(),
        headlineSmall = base.headlineSmall.rounded(),
        titleLarge = base.titleLarge.rounded(),
        titleMedium = base.titleMedium.rounded(),
        titleSmall = base.titleSmall.rounded(),
        bodyLarge = base.bodyLarge.rounded(),
        bodyMedium = base.bodyMedium.rounded(),
        bodySmall = base.bodySmall.rounded(),
        labelLarge = base.labelLarge.rounded(),
        labelMedium = base.labelMedium.rounded(),
        labelSmall = base.labelSmall.rounded(),
    )
}

// Fallback for Android < 12 (no dynamic color). Replace with a brand scheme generated from
// a seed color (Material Theme Builder) if the app has one.
private val FallbackLight: ColorScheme = lightColorScheme()
private val FallbackDark: ColorScheme = darkColorScheme()

@Composable
fun AppTheme(
    themeMode: ThemeMode = ThemeMode.System,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val darkTheme = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> FallbackDark
        else -> FallbackLight
    }

    // enableEdgeToEdge() makes the bars transparent; here we only flip icon contrast when the
    // in-app theme differs from the system theme.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}

/** Darken a color, e.g. to derive an icon tint from a pastel badge color. */
fun Color.darken(factor: Float = 0.7f): Color =
    Color(red = red * factor, green = green * factor, blue = blue * factor, alpha = alpha)
