package dev.jonpoulton.actual.login.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.jonpoulton.actual.core.coroutines.ResettableStateFlow
import dev.jonpoulton.actual.core.model.ActualVersions
import dev.jonpoulton.actual.core.model.Password
import dev.jonpoulton.actual.core.model.ServerUrl
import dev.jonpoulton.actual.core.state.ActualVersionsStateHolder
import dev.jonpoulton.actual.login.prefs.LoginPreferences
import dev.jonpoulton.actual.serverurl.prefs.ServerUrlPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject internal constructor(
  private val loginRequester: LoginRequester,
  versionsStateHolder: ActualVersionsStateHolder,
  serverUrlPrefs: ServerUrlPreferences,
  loginPrefs: LoginPreferences,
) : ViewModel() {
  private val mutableEnteredPassword = ResettableStateFlow(Password.Empty)
  private val mutableIsLoading = ResettableStateFlow(false)
  private val mutableLoginFailure = ResettableStateFlow<LoginResult.Failure?>(null)

  val enteredPassword: StateFlow<Password> = mutableEnteredPassword.asStateFlow()

  val versions: StateFlow<ActualVersions> = versionsStateHolder.state

  val serverUrl: StateFlow<ServerUrl> = serverUrlPrefs
    .url
    .asFlow()
    .filterNotNull()
    .stateIn(viewModelScope, SharingStarted.Eagerly, ServerUrl.Demo)

  val isLoading: StateFlow<Boolean> = mutableIsLoading.asStateFlow()

  val loginFailure: StateFlow<LoginResult.Failure?> =
    combine(mutableLoginFailure, isLoading) { failure, loading -> if (loading) null else failure }
      .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)

  val navToBudgetList: StateFlow<Boolean> = loginPrefs
    .token
    .asFlow()
    .map { it != null }
    .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = false)

  fun clearState() {
    mutableEnteredPassword.reset()
    mutableIsLoading.reset()
    mutableLoginFailure.reset()
  }

  fun onPasswordEntered(password: String) {
    mutableEnteredPassword.update { Password(password) }
  }

  fun onClickSignIn() {
    Timber.v("onClickSignIn")
    mutableIsLoading.update { true }
    mutableLoginFailure.reset()
    viewModelScope.launch {
      val password = mutableEnteredPassword.value
      val result = loginRequester.logIn(password)

      Timber.d("Login result = $result")
      mutableIsLoading.update { false }

      if (result is LoginResult.Failure) {
        mutableLoginFailure.update { result }
      }
    }
  }
}
