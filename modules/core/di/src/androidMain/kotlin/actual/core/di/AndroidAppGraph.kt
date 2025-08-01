package actual.core.di

import alakazam.kotlin.core.BuildConfig
import android.app.Activity
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.Provides
import kotlin.reflect.KClass

@DependencyGraph(AppScope::class)
interface AndroidAppGraph {
  // TODO: remove allowEmpty once we have activities set up
  @Multibinds(allowEmpty = true)
  val activityProviders: Map<KClass<out Activity>, Provider<Activity>>

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(
      @Provides context: Context,
      @Provides buildConfig: BuildConfig,
      @Includes debug: DebugContainer,
    ): AndroidAppGraph
  }

  fun interface Holder {
    fun graph(): AndroidAppGraph
  }
}
