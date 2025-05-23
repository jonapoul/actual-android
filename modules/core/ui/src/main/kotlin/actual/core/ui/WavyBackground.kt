@file:Suppress("CompositionLocalAllowlist")

package actual.core.ui

import actual.core.model.ColorSchemeType
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import actual.core.res.R as CoreR

val LocalColorSchemeType = compositionLocalOf<ColorSchemeType> { error("No ColorSchemeType defined!") }

@Composable
fun WavyBackground(
  modifier: Modifier = Modifier,
  schemeType: ColorSchemeType = LocalColorSchemeType.current,
) {
  Image(
    modifier = modifier.fillMaxSize(),
    painter = schemeType.backgroundImage(),
    contentDescription = null,
    contentScale = ContentScale.FillHeight,
  )
}

@Stable
@Composable
private fun ColorSchemeType.backgroundImage() = painterResource(
  when (this) {
    ColorSchemeType.Light -> CoreR.mipmap.bg_light
    ColorSchemeType.Dark -> CoreR.mipmap.bg_dark
    ColorSchemeType.Midnight -> CoreR.mipmap.bg_midnight
    ColorSchemeType.System -> if (isSystemInDarkTheme()) CoreR.mipmap.bg_dark else CoreR.mipmap.bg_light
  },
)

@Preview
@Composable
private fun BackgroundImage() = PreviewColumn { type ->
  WavyBackground(schemeType = type)
}

@ScreenPreview
@Composable
private fun Scaled() = PreviewActualRow { type ->
  WavyBackground(
    modifier = Modifier.width(MY_PHONE_WIDTH_DP.dp),
    schemeType = type,
  )
}
