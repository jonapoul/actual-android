package actual.core.di

import alakazam.kotlin.core.InfiniteLoopController
import alakazam.kotlin.core.LoopController
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface LoopControllerGraph {
  @Binds val InfiniteLoopController.bind: LoopController
}
