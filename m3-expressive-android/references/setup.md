# Project setup

## Dependencies

Versions below are what the reference app (Med) builds with as of late 2026. Prefer the newest stable/beta the user's project can take; the minimums matter more than the exact numbers.

| Library | Minimum | Why |
|---|---|---|
| `androidx.compose.material3:material3` | 1.4.0 (1.5.0-alpha+ for `SegmentedListItem`) | Expressive APIs: `MaterialExpressiveTheme`, `MotionScheme`, `ToggleButton`, `ButtonGroupDefaults`, `ShortNavigationBar`, `WideNavigationRail`, `FloatingActionButtonMenu`, `HorizontalFloatingToolbar`, `LoadingIndicator`, `MaterialShapes` |
| `androidx.compose.material:material-icons-extended` | 1.7.x | `Icons.Rounded.*` |
| `androidx.compose.material3:material3-window-size-class` | 1.4.0 | compact vs expanded layouts |
| `androidx.compose.material3:material3-adaptive-navigation-suite` | optional | if you prefer `NavigationSuiteScaffold` over manual bar/rail switching |
| `androidx.graphics:graphics-shapes` | 1.0.0 | `RoundedPolygon` / shape morphing |
| `androidx.core:core-splashscreen` | 1.0.1 | `installSplashScreen()` |
| `androidx.activity:activity-compose` | 1.9+ | `enableEdgeToEdge()` |
| Compose BOM | recent | keep ui/foundation aligned |

`libs.versions.toml` excerpt (as in Med):

```toml
[versions]
composeBom = "2026.09.00"
material3 = "1.5.0-beta01"
materialIconsExtended = "1.7.8"
graphicsShapes = "1.1.0"
coreSplashscreen = "1.2.0"
activityCompose = "1.13.0"

[libraries]
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3", version.ref = "material3" }
androidx-compose-material3-window-size-class = { module = "androidx.compose.material3:material3-window-size-class", version.ref = "material3" }
androidx-compose-material-icons-extended = { module = "androidx.compose.material:material-icons-extended", version.ref = "materialIconsExtended" }
androidx-graphics-shapes = { group = "androidx.graphics", name = "graphics-shapes", version.ref = "graphicsShapes" }
androidx-core-splashscreen = { group = "androidx.core", name = "core-splashscreen", version.ref = "coreSplashscreen" }
androidx-activity-compose = { group = "androidx.activity", name = "activity-compose", version.ref = "activityCompose" }
```

If a material3 version is pinned explicitly, it overrides the BOM's material3; that's intended (the BOM usually lags the expressive alphas/betas).

minSdk 26 works fine; dynamic color is gated at runtime on API 31.

## Opt-ins

Put at the top of every file that uses expressive components:

```kotlin
@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    androidx.compose.ui.text.ExperimentalTextApi::class, // FontVariation on the variable font
)
```

## Font

1. Put the variable TTF in `app/src/main/res/font/` with a lowercase underscore name, e.g. `google_sans_flex.ttf`.
2. `Theme.kt` (template) declares several `Font(...)` entries pointing at the same file with different `FontVariation.weight(...)` values plus `FontVariation.Setting("ROND", 100f)`. Declaring 400/500/700 separately lets Compose pick a real weight instead of faking bold.
3. Variable-font axes need API 26+; below that the font renders at its default instance.
4. Check the font's license before shipping. If you can't ship Google Sans Flex, use Roboto Flex (OFL, on Google Fonts) and drop the `ROND` setting.

## Activity boilerplate

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val windowSize = calculateWindowSizeClass(this)
                AppRoot(isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded)
            }
        }
    }
}
```

Desktop windows (API 35+): make the caption bar transparent so the top bar color runs under it, and pad the home header with `WindowInsets.captionBar`:

```kotlin
LaunchedEffect(Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        activity.window?.insetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND,
            WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND
        )
    }
}
```

Splash screen theme (`res/values/themes.xml`): parent `Theme.SplashScreen`, `windowSplashScreenAnimatedIcon` = monochrome-capable adaptive icon, `postSplashScreenTheme` = a `Theme.Material3.DayNight.NoActionBar` descendant. Ship a monochrome launcher layer so themed icons work on Android 13+.

## Theme switching (System / Light / Dark)

The template `AppTheme(themeOverride: ThemeMode?)` takes the user's choice. When the user changes it on the settings screen, wrap the content in `Crossfade(targetState = mode, animationSpec = tween(350))` and re-enter `AppTheme(themeOverride = it)` so the whole screen dissolves into the new palette instead of snapping.
