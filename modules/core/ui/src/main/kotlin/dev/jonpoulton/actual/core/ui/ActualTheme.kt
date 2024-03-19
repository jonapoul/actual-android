package dev.jonpoulton.actual.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun ActualTheme(
  schemeType: ActualColorSchemeType = ActualColorSchemeType.System,
  content: @Composable () -> Unit,
) {
  // TODO: Handle user preferences here

  val systemDarkTheme = isSystemInDarkTheme()

  val colorScheme = when (schemeType) {
    ActualColorSchemeType.System -> if (systemDarkTheme) DarkColorScheme else LightColorScheme
    ActualColorSchemeType.Dark -> DarkColorScheme
    ActualColorSchemeType.Light -> LightColorScheme
    ActualColorSchemeType.Midnight -> MidnightColorScheme
  }

  CompositionLocalProvider(
    LocalActualColorScheme provides colorScheme,
  ) {
    SetStatusBarColors(
      darkTheme = systemDarkTheme,
    )

    val materialColorScheme = when (colorScheme) {
      LightColorScheme -> lightColorScheme()
      DarkColorScheme -> darkColorScheme()
      MidnightColorScheme -> darkColorScheme(surface = Color.Black)
    }

    MaterialTheme(
      colorScheme = materialColorScheme,
      typography = ActualTypography,
      content = content,
    )
  }
}
