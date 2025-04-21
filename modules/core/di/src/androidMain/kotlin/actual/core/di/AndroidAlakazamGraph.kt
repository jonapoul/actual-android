package actual.core.di

import alakazam.android.core.UrlOpener
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides

@DependencyGraph(AppScope::class)
interface AndroidAlakazamGraph {
  val urlOpener: UrlOpener

  @Provides
  fun url(context: Context): UrlOpener = UrlOpener(context)

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(@Includes ctx: AndroidContextGraph): AndroidAlakazamGraph
  }
}
