package actual.url.prefs

import actual.test.buildPreferences
import actual.url.model.Protocol
import actual.url.model.ServerUrl
import alakazam.test.core.MainDispatcherRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class ServerUrlPreferencesTest {
  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private lateinit var preferences: ServerUrlPreferences

  @Before
  fun before() {
    val prefs = buildPreferences(mainDispatcherRule.dispatcher)
    preferences = ServerUrlPreferences(prefs)
  }

  @Test
  fun `Emit URLs`() = runTest {
    preferences.url.asFlow().test {
      // Given
      assertNull(awaitItem())

      // When
      val url1 = ServerUrl(protocol = Protocol.Https, baseUrl = "website.com")
      preferences.url.set(url1)

      // Then
      assertEquals(expected = url1, actual = awaitItem())

      // When
      val url2 = ServerUrl(protocol = Protocol.Http, baseUrl = "some.other.domain.co.uk")
      preferences.url.set(url2)

      // Then
      assertEquals(expected = url2, actual = awaitItem())

      // When
      preferences.url.delete()

      // Then
      assertNull(awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }
}
