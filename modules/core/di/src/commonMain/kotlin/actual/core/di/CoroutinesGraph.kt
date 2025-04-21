package actual.core.di

import alakazam.kotlin.core.CoroutineContexts
import alakazam.kotlin.core.DefaultCoroutineContexts
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@DependencyGraph(AppScope::class)
interface CoroutinesGraph {
  @Provides
  @SingleIn(AppScope::class)
  fun scope(): CoroutineScope = CoroutineScope(SupervisorJob())

  @Provides
  @SingleIn(AppScope::class)
  fun contexts(): CoroutineContexts = DefaultCoroutineContexts()
}
