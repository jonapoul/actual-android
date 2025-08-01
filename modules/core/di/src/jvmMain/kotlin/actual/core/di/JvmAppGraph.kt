package actual.core.di

import alakazam.kotlin.core.BuildConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface JvmAppGraph {
  @DependencyGraph.Factory
  fun interface Factory {
    fun create(
      @Provides buildConfig: BuildConfig,
      @Includes debug: DebugContainer,
    ): JvmAppGraph
  }

  fun interface Holder {
    fun graph(): JvmAppGraph
  }
}
