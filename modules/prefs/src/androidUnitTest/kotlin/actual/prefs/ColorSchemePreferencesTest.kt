package actual.prefs

import actual.core.model.ColorSchemeType.Dark
import actual.core.model.ColorSchemeType.Light
import actual.core.model.ColorSchemeType.System
import actual.test.assertEmitted
import actual.test.buildPreferences
import alakazam.test.core.MainDispatcherRule
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class ColorSchemePreferencesTest {
  @get:Rule val mainDispatcherRule = MainDispatcherRule()

  private lateinit var preferences: ColorSchemePreferences

  @BeforeTest
  fun before() {
    val prefs = buildPreferences(mainDispatcherRule.dispatcher)
    preferences = ColorSchemePreferences(prefs)
  }

  @Test
  fun `Emit types`() = runTest {
    with(preferences.type) {
      asFlow().test {
        // Given
        assertEmitted(System)

        set(Light)
        assertEmitted(Light)

        set(System)
        assertEmitted(System)

        set(Dark)
        assertEmitted(Dark)

        deleteAndCommit()
        assertEmitted(System)

        expectNoEvents()
        cancelAndIgnoreRemainingEvents()
      }
    }
  }
}
