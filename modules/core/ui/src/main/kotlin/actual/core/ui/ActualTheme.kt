package actual.core.ui

import actual.core.model.ColorSchemeType
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

@Composable
fun ActualTheme(
  schemeType: ColorSchemeType,
  content: ComposableLambda,
) {
  val systemDarkTheme = isSystemInDarkTheme()

  val theme = remember(schemeType, systemDarkTheme) {
    when (schemeType) {
      ColorSchemeType.System -> if (systemDarkTheme) DarkTheme() else LightTheme()
      ColorSchemeType.Dark -> DarkTheme()
      ColorSchemeType.Light -> LightTheme()
      ColorSchemeType.Midnight -> MidnightTheme()
    }
  }

  CompositionLocalProvider(
    LocalTheme provides theme,
  ) {
    SetStatusBarColors(
      theme = theme,
    )

    val materialColorScheme = when (theme) {
      is LightTheme -> lightColorScheme()
      is DarkTheme -> darkColorScheme()
      is MidnightTheme -> darkColorScheme(surface = Color.Black)
    }

    MaterialTheme(
      colorScheme = materialColorScheme,
      typography = typography(theme),
      content = content,
    )
  }
}
