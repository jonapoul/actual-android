package actual.core.di

import alakazam.kotlin.core.CoroutineContexts
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dev.jonpoulton.preferences.android.AndroidSharedPreferences
import dev.jonpoulton.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AndroidPreferencesGraph {
  @Provides
  fun sharedPrefs(
    context: Context,
  ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

  @Provides
  fun prefs(
    prefs: SharedPreferences,
    contexts: CoroutineContexts,
  ): Preferences = AndroidSharedPreferences(prefs, contexts.io)

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(
      @Includes ctx: AndroidContextGraph,
      @Includes coroutines: CoroutinesGraph,
    ): AndroidPreferencesGraph
  }
}
