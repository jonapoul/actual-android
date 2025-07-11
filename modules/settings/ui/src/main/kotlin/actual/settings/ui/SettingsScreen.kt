package actual.settings.ui

import actual.core.model.DarkColorSchemeType
import actual.core.model.RegularColorSchemeType
import actual.core.ui.LocalTheme
import actual.core.ui.PreviewScreen
import actual.core.ui.ScreenPreview
import actual.core.ui.Theme
import actual.core.ui.WavyBackground
import actual.core.ui.scrollbarSettings
import actual.core.ui.transparentTopAppBarColors
import actual.l10n.Dimens
import actual.l10n.Strings
import actual.settings.ui.items.ShowBottomBarPreferenceItem
import actual.settings.ui.items.ThemePreferenceItem
import actual.settings.vm.PreferenceValue
import actual.settings.vm.SettingsViewModel
import actual.settings.vm.ThemeConfig
import alakazam.android.ui.compose.VerticalSpacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import my.nanihadesuka.compose.LazyColumnScrollbar

@Composable
fun SettingsScreen(
  nav: SettingsNavigator,
  viewModel: SettingsViewModel = hiltViewModel(),
) {
  val values by viewModel.prefValues.collectAsState()

  SettingsScaffold(
    values = values,
    onAction = { action ->
      when (action) {
        SettingsAction.NavBack -> nav.back()
        is SettingsAction.PreferenceChange -> viewModel.set(action.value)
      }
    },
  )
}

@Composable
private fun SettingsScaffold(
  values: ImmutableList<PreferenceValue>,
  onAction: (SettingsAction) -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
  val theme = LocalTheme.current

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      TopAppBar(
        colors = theme.transparentTopAppBarColors(),
        navigationIcon = {
          IconButton(onClick = { onAction(SettingsAction.NavBack) }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = Strings.navBack,
            )
          }
        },
        title = { Text(Strings.settingsToolbar) },
        scrollBehavior = scrollBehavior,
      )
    },
  ) { innerPadding ->
    Box {
      val hazeState = remember { HazeState() }

      WavyBackground(
        modifier = Modifier.hazeSource(hazeState),
      )

      SettingsContent(
        modifier = Modifier.padding(innerPadding),
        values = values,
        hazeState = hazeState,
        onAction = onAction,
        theme = theme,
      )
    }
  }
}

@Stable
@Composable
private fun SettingsContent(
  values: ImmutableList<PreferenceValue>,
  hazeState: HazeState,
  onAction: (SettingsAction) -> Unit,
  theme: Theme,
  modifier: Modifier = Modifier,
) {
  val listState = rememberLazyListState()
  LazyColumnScrollbar(
    modifier = modifier
      .fillMaxSize()
      .padding(Dimens.large),
    state = listState,
    settings = theme.scrollbarSettings(),
  ) {
    LazyColumn(
      state = listState,
    ) {
      itemsIndexed(values) { i, value ->
        PreferenceItem(
          modifier = Modifier.fillMaxWidth(),
          value = value,
          hazeState = hazeState,
          onChange = { onAction(SettingsAction.PreferenceChange(it)) },
        )

        if (i != values.size - 1) {
          VerticalSpacer(10.dp)
        }
      }
    }
  }
}

@Composable
private fun PreferenceItem(
  value: PreferenceValue,
  hazeState: HazeState,
  onChange: (PreferenceValue) -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier,
  ) {
    when (value) {
      is PreferenceValue.Theme -> ThemePreferenceItem(
        config = value.config,
        hazeState = hazeState,
        onChange = { onChange(PreferenceValue.Theme(it)) },
      )

      is PreferenceValue.ShowBottomBar -> ShowBottomBarPreferenceItem(
        value = value.show,
        hazeState = hazeState,
        onChange = { onChange(PreferenceValue.ShowBottomBar(it)) },
      )
    }
  }
}

@ScreenPreview
@Composable
private fun Regular() = PreviewScreen { type ->
  SettingsScaffold(
    onAction = {},
    values = persistentListOf(
      PreferenceValue.Theme(ThemeConfig(RegularColorSchemeType.Dark, DarkColorSchemeType.Dark)),
      PreferenceValue.ShowBottomBar(false),
    ),
  )
}
