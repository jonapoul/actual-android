@file:OptIn(ExperimentalCoroutinesApi::class)

package actual.core.connection

import actual.api.client.ActualApis
import actual.api.client.ActualApisStateHolder
import actual.api.client.BaseApi
import actual.api.model.base.Build
import actual.api.model.base.InfoResponse
import actual.core.model.ActualVersions
import actual.core.model.ActualVersionsStateHolder
import actual.test.TestBuildConfig
import alakazam.kotlin.core.LoopController
import alakazam.test.core.FiniteLoopController
import alakazam.test.core.MainDispatcherRule
import alakazam.test.core.SingleLoopController
import alakazam.test.core.TestCoroutineContexts
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class ServerVersionFetcherTest {
  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  // real
  private lateinit var fetcher: ServerVersionFetcher
  private lateinit var apisStateHolder: ActualApisStateHolder
  private lateinit var versionsStateHolder: ActualVersionsStateHolder

  // mock
  private lateinit var apis: ActualApis
  private lateinit var baseApi: BaseApi

  @BeforeTest
  fun before() {
    baseApi = mockk()
    apis = mockk {
      every { base } returns baseApi
      every { close() } just runs
    }

    apisStateHolder = ActualApisStateHolder()
    apisStateHolder.update { apis }

    versionsStateHolder = ActualVersionsStateHolder(TestBuildConfig)

    build()
  }

  private fun build(
    loopController: LoopController = SingleLoopController(),
  ) {
    fetcher = ServerVersionFetcher(
      contexts = TestCoroutineContexts(mainDispatcherRule),
      apisStateHolder = apisStateHolder,
      versionsStateHolder = versionsStateHolder,
      loopController = loopController,
    )
  }

  @Test
  fun `No APIs means nothing is fetched`() = runTest(timeout = 5.seconds) {
    // Given
    apisStateHolder.reset()
    advanceUntilIdle()

    // When
    val fetchJob = launch { fetcher.startFetching() }

    advanceUntilIdle()
    versionsStateHolder.test {
      // Then
      assertEquals(expected = emptyState(), actual = awaitItem())
      advanceUntilIdle()

      expectNoEvents()
      cancelAndIgnoreRemainingEvents()
      fetchJob.cancel()
    }
  }

  @Test
  fun `Valid fetch response`() = runTest(timeout = 5.seconds) {
    // Given
    coEvery { baseApi.fetchInfo() } returns InfoResponse(
      build = Build(
        name = "ABC",
        description = "XYZ",
        version = "1.2.3",
      ),
    )

    // When
    versionsStateHolder.test {
      assertEquals(expected = emptyState(), actual = awaitItem())

      val fetchJob = launch { fetcher.startFetching() }

      // Then
      assertEquals(expected = "1.2.3", actual = awaitItem().server)
      cancelAndIgnoreRemainingEvents()
      fetchJob.cancel()
    }
  }

  @Test
  fun `Failed then successful fetch response`() = runTest(timeout = 5.seconds) {
    // Given
    val validResponse = InfoResponse(build = Build(name = "ABC", description = "XYZ", version = "1.2.3"))
    val failureReason = "SOMETHING BROKE"
    coEvery { baseApi.fetchInfo() } answers {
      coEvery { baseApi.fetchInfo() } returns validResponse
      throw IOException(failureReason)
    }

    val loopController = FiniteLoopController(maxLoops = 2)
    build(loopController)

    // When
    versionsStateHolder.test {
      assertEquals(expected = emptyState(), actual = awaitItem())

      val fetchJob = launch { fetcher.startFetching() }

      // Then
      assertEquals(expected = "1.2.3", actual = awaitItem().server)
      cancelAndIgnoreRemainingEvents()
      fetchJob.cancel()
    }
  }

  private fun emptyState() = ActualVersions(TestBuildConfig.versionName, server = null)
}
