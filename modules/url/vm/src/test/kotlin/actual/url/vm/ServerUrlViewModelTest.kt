@file:OptIn(ExperimentalCoroutinesApi::class)

package actual.url.vm

import actual.account.login.domain.LoginPreferences
import actual.account.model.LoginMethod
import actual.account.model.LoginToken
import actual.api.client.AccountApi
import actual.api.client.ActualApis
import actual.api.client.ActualApisStateHolder
import actual.api.model.account.NeedsBootstrapResponse
import actual.core.versions.ActualVersionsStateHolder
import actual.test.TestBuildConfig
import actual.test.assertEmitted
import actual.test.buildPreferences
import actual.url.model.Protocol
import actual.url.model.ServerUrl
import actual.url.prefs.ServerUrlPreferences
import alakazam.test.core.MainDispatcherRule
import alakazam.test.core.TestCoroutineContexts
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class ServerUrlViewModelTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  // Real
  private lateinit var serverUrlPreferences: ServerUrlPreferences
  private lateinit var loginPreferences: LoginPreferences
  private lateinit var viewModel: ServerUrlViewModel
  private lateinit var apisStateHolder: ActualApisStateHolder
  private lateinit var versionsStateHolder: ActualVersionsStateHolder

  // Mock
  private lateinit var apis: ActualApis
  private lateinit var accountApi: AccountApi

  @Before
  fun before() {
    val prefs = buildPreferences(mainDispatcherRule.dispatcher)
    serverUrlPreferences = ServerUrlPreferences(prefs)
    serverUrlPreferences.url.set(EXAMPLE_URL)
    loginPreferences = LoginPreferences(prefs)
    versionsStateHolder = ActualVersionsStateHolder(TestBuildConfig)

    accountApi = mockk()
    apis = mockk {
      every { serverUrl } returns EXAMPLE_URL
      every { account } returns accountApi
    }
    apisStateHolder = ActualApisStateHolder()
    apisStateHolder.update { apis }
  }

  private fun buildViewModel() {
    viewModel = ServerUrlViewModel(
      contexts = TestCoroutineContexts(mainDispatcherRule),
      apiStateHolder = apisStateHolder,
      serverUrlPreferences = serverUrlPreferences,
      loginPreferences = loginPreferences,
      versionsStateHolder = versionsStateHolder,
      urlProvider = { null },
    )
  }

  @Test
  fun `Nav to login when typing and clicking confirm if already bootstrapped`() = runTest {
    buildViewModel()
    viewModel.navDestination.receiveAsFlow().test {
      // Given we're currently not navigating, and the API returns that we're bootstrapped
      advanceUntilIdle()
      ensureAllEventsConsumed()
      setBootstrapResponse(bootstrapped = true)

      // When
      viewModel.onSelectProtocol(EXAMPLE_URL.protocol)
      viewModel.onEnterUrl(EXAMPLE_URL.baseUrl)
      viewModel.onClickConfirm()

      // Then
      assertEquals(expected = NavDestination.ToLogin, actual = awaitItem())
      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Nav to bootstrap when typing and clicking confirm if not bootstrapped`() = runTest {
    buildViewModel()
    viewModel.navDestination.receiveAsFlow().test {
      // Given we're currently not navigating, and the API returns that we're not bootstrapped
      advanceUntilIdle()
      ensureAllEventsConsumed()
      setBootstrapResponse(bootstrapped = false)

      // When
      viewModel.onSelectProtocol(EXAMPLE_URL.protocol)
      viewModel.onEnterUrl(EXAMPLE_URL.baseUrl)
      viewModel.onClickConfirm()

      // Then
      assertEquals(expected = NavDestination.ToBootstrap, actual = awaitItem())
      advanceUntilIdle()
      ensureAllEventsConsumed()
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Set initial parameters based on preferences`() = runTest {
    // Given
    serverUrlPreferences.url.set(EXAMPLE_URL)

    // When
    buildViewModel()
    advanceUntilIdle()

    // Then
    val protocol = viewModel.protocol.value
    val baseUrl = viewModel.baseUrl.value
    assertEquals(expected = EXAMPLE_URL.protocol, actual = protocol)
    assertEquals(expected = EXAMPLE_URL.baseUrl, actual = baseUrl)
  }

  @Test
  fun `Show error message if bootstrap request gives failure response`() = runTest {
    buildViewModel()
    viewModel.errorMessage.test {
      // Given we're currently not navigating, and the API returns that we're not bootstrapped
      assertNull(awaitItem())
      val reason = "SOMETHING BROKE"
      val body = """
        {
          "status": "error",
          "reason": "$reason"
        }
      """.trimIndent()
      coEvery { accountApi.needsBootstrap() } returns Response.error(400, body.toResponseBody())

      // When
      viewModel.onSelectProtocol(EXAMPLE_URL.protocol)
      viewModel.onEnterUrl(EXAMPLE_URL.baseUrl)
      viewModel.onClickConfirm()

      // Then
      val emitted = awaitItem()
      assertEquals(
        expected = true,
        actual = emitted?.contains(reason),
        message = "Received $emitted",
      )
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Show error message if bootstrap request fails`() = runTest {
    buildViewModel()
    viewModel.errorMessage.test {
      // Given we're currently not navigating, and the API returns that we're not bootstrapped
      assertNull(awaitItem())
      val reason = "SOMETHING BROKE"
      coEvery { accountApi.needsBootstrap() } throws IOException(reason)

      // When
      viewModel.onSelectProtocol(EXAMPLE_URL.protocol)
      viewModel.onEnterUrl(EXAMPLE_URL.baseUrl)
      viewModel.onClickConfirm()

      // Then
      val emitted = awaitItem()
      assertEquals(
        expected = true,
        actual = emitted?.contains(reason),
        message = "Received $emitted",
      )
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `Clear saved token if the confirmed URL is different from previously-saved`() = runTest {
    buildViewModel()
    loginPreferences.token.asFlow().test {
      // Given no token initially saved
      assertNull(awaitItem())

      // when we save a token and a URL
      val initialUrl = ServerUrl(Protocol.Https, "website.com")
      serverUrlPreferences.url.setAndCommit(initialUrl)
      val token = LoginToken("abc-123")
      loginPreferences.token.setAndCommit(token)

      // then the token has been saved
      assertEmitted(token)

      // when we enter a different url and click confirm
      val secondUrl = ServerUrl(Protocol.Http, "some.other.website.com")
      assertNotEquals(initialUrl, secondUrl)
      viewModel.onSelectProtocol(secondUrl.protocol)
      viewModel.onEnterUrl(secondUrl.baseUrl)
      viewModel.onClickConfirm()

      // then the saved token is cleared
      assertNull(awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  private fun setBootstrapResponse(bootstrapped: Boolean) {
    coEvery { accountApi.needsBootstrap() } returns Response.success(
      NeedsBootstrapResponse.Success(
        data = NeedsBootstrapResponse.Data(
          bootstrapped = bootstrapped,
          loginMethod = LoginMethod.Password,
          availableLoginMethods = emptyList(),
        ),
      ),
    )
  }

  private companion object {
    val EXAMPLE_URL = ServerUrl(Protocol.Http, "website.com")
  }
}
