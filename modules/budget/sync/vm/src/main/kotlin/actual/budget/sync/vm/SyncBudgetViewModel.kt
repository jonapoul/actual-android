package actual.budget.sync.vm

import actual.account.model.LoginToken
import alakazam.kotlin.logging.Logger
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = SyncBudgetViewModel.Factory::class)
class SyncBudgetViewModel @AssistedInject constructor(
  @Assisted tokenString: String,
) : ViewModel() {
  // Necessary because trying to pass a value class through dagger's assisted injection results in a KSP build failure.
  // See https://github.com/google/dagger/issues/4613
  // TODO: Rework to pass the token in directly when they fix it
  @Suppress("UnusedPrivateProperty")
  private val token = LoginToken(tokenString)

  private val mutableState = MutableStateFlow<SyncState>(SyncState.TalkingToServer)
  val state: StateFlow<SyncState> = mutableState.asStateFlow()

  init {
    Logger.d("init")
  }

  fun retry() {
    Logger.d("retry")
  }

  @AssistedFactory
  interface Factory {
    fun create(tokenString: String): SyncBudgetViewModel
  }
}
