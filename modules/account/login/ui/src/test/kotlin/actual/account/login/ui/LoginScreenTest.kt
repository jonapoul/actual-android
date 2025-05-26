package actual.account.login.ui

import actual.account.login.domain.LoginRequester
import actual.account.login.domain.LoginResult
import actual.account.login.vm.LoginViewModel
import actual.account.model.LoginToken
import actual.account.model.Password
import actual.core.model.ActualVersionsStateHolder
import actual.prefs.LoginPreferences
import actual.prefs.ServerUrlPreferences
import actual.test.TestBuildConfig
import actual.test.assertEditableTextEquals
import actual.test.buildPreferences
import actual.test.runTest
import actual.test.setThemedContent
import alakazam.test.core.MainDispatcherRule
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class LoginScreenTest {
  @get:Rule val mainDispatcherRule = MainDispatcherRule()
  @get:Rule val composeRule = createComposeRule()

  // real
  private lateinit var viewModel: LoginViewModel
  private lateinit var versionsStateHolder: ActualVersionsStateHolder
  private lateinit var serverPrefs: ServerUrlPreferences
  private lateinit var loginPrefs: LoginPreferences

  // mock
  private lateinit var navigator: LoginNavigator
  private lateinit var loginRequester: LoginRequester

  @BeforeTest
  fun before() {
    ShadowLog.stream = System.out

    navigator = mockk(relaxed = true)
    loginRequester = mockk()
    setLoginResult { LoginResult.Success(TOKEN) }

    versionsStateHolder = ActualVersionsStateHolder(BUILD_CONFIG)
    val prefs = buildPreferences(mainDispatcherRule.dispatcher)
    serverPrefs = ServerUrlPreferences(prefs)
    loginPrefs = LoginPreferences(prefs)
  }

  private fun buildViewModel(password: Password = Password.Empty) {
    viewModel = LoginViewModel(
      loginRequester = loginRequester,
      versionsStateHolder = versionsStateHolder,
      serverUrlPrefs = serverPrefs,
      loginPrefs = loginPrefs,
      passwordProvider = { password },
    )
  }

  @Test
  fun `Login success`() = composeRule.runTest {
    // given initial state with empty password
    buildViewModel(password = Password.Empty)
    setThemedContent { LoginScreen(navigator, viewModel) }

    // and the login takes half a second before succeeding
    setLoginResult {
      loginPrefs.token.setAndCommit(TOKEN) // to trigger nav behaviour
      LoginResult.Success(TOKEN)
    }

    // then then text is empty
    onNodeWithTag(Tags.PasswordLoginTextField)
      .assertIsEnabled()
      .assertEditableTextEquals("")

    // and the login button is disabled
    onNodeWithTag(Tags.PasswordLoginButton, useUnmergedTree = true).performClick()
    runOnIdle { coVerify(exactly = 0) { loginRequester.logIn(any()) } }

    // when we enter a password
    onNodeWithTag(Tags.PasswordLoginTextField)
      .performTextInput(PASSWORD.value)

    // and hit enter on the keyboard, with hidden password text visible
    onNodeWithTag(Tags.PasswordLoginTextField)
      .assertEditableTextEquals("•••••••••")
      .performImeAction()

    runOnIdle {
      // and the login was triggered
      coVerify(exactly = 1) { loginRequester.logIn(PASSWORD) }

      // and the navigation was triggered when it succeeded
      verify(exactly = 1) { navigator.toListBudgets(TOKEN) }
    }
  }

  @Test
  fun `Login failure`() = composeRule.runTest {
    // given initial state with dummy password
    buildViewModel(password = PASSWORD)
    setThemedContent { LoginScreen(navigator, viewModel) }

    // and the login takes half a second before failing
    setLoginResult { LoginResult.HttpFailure(code = 404, message = "It failed") }

    // When we hit enter on the keyboard, with hidden password text visible
    onNodeWithTag(Tags.PasswordLoginTextField)
      .assertEditableTextEquals("•••••••••")
      .performImeAction()

    runOnIdle {
      // then the login was triggered
      coVerify(exactly = 1) { loginRequester.logIn(PASSWORD) }

      // and the navigation was not triggered
      verify(exactly = 0) { navigator.toListBudgets(TOKEN) }
    }

    // and the login failure text is visible
    waitUntilExactlyOneExists(hasTestTag(Tags.LoginFailureText))
    onNodeWithTag(Tags.LoginFailureText).assertTextEquals("Server error 404: It failed")
  }

  private fun setLoginResult(block: suspend () -> LoginResult) {
    coEvery { loginRequester.logIn(any()) } coAnswers { block() }
  }

  private companion object {
    val PASSWORD = Password("P@ssw0rd!")
    val TOKEN = LoginToken("abc-123")

    val BUILD_CONFIG = TestBuildConfig.copy(
      versionName = "1.2.3",
      versionCode = 123,
    )
  }
}
