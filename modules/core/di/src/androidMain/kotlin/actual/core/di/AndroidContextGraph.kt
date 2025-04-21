package actual.core.di

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AndroidContextGraph {
  val context: Context

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Provides context: Context): AndroidContextGraph
  }
}
