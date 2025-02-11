package actual.budget.list.vm

import actual.core.colorscheme.ColorSchemePreferences
import actual.core.colorscheme.ColorSchemeType
import actual.core.versions.ActualVersions
import actual.core.versions.ActualVersionsStateHolder
import actual.login.prefs.LoginPreferences
import actual.url.model.ServerUrl
import actual.url.prefs.ServerUrlPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// TODO: Remove suppression
@Suppress("UNUSED_PARAMETER")
@HiltViewModel
class ListBudgetsViewModel @Inject internal constructor(
  serverUrlPrefs: ServerUrlPreferences,
  loginPrefs: LoginPreferences,
  versionsStateHolder: ActualVersionsStateHolder,
  colorSchemePreferences: ColorSchemePreferences,
) : ViewModel() {
  val versions: StateFlow<ActualVersions> = versionsStateHolder.asStateFlow()

  val serverUrl: StateFlow<ServerUrl> = serverUrlPrefs
    .url
    .asFlow()
    .filterNotNull()
    .stateIn(viewModelScope, SharingStarted.Eagerly, ServerUrl.Demo)

  val themeType: StateFlow<ColorSchemeType> = colorSchemePreferences.stateFlow(viewModelScope)

  val state: StateFlow<ListBudgetsState> = flowOf(ListBudgetsState.Loading)
    .stateIn(viewModelScope, SharingStarted.Eagerly, ListBudgetsState.Loading)

  fun clearState() {
    // TBC
  }
}
