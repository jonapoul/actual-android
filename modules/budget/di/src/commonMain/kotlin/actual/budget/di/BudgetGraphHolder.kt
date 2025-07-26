package actual.budget.di

import actual.budget.model.BudgetFiles
import actual.budget.model.DbMetadata
import alakazam.kotlin.core.CoroutineContexts
import alakazam.kotlin.core.StateHolder
import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

@Inject
@SingleIn(AppScope::class)
class BudgetGraphHolder(
  private val context: Context,
  private val files: BudgetFiles,
  private val scope: CoroutineScope,
  private val contexts: CoroutineContexts,
) : StateHolder<BudgetGraph?>(initialState = null), AutoCloseable {
  fun require(): BudgetGraph = requireNotNull(value) { "No budget graph loaded!" }

  fun clear() = update { null }

  fun update(metadata: DbMetadata): BudgetGraph {
    val component = BudgetGraph.build(metadata, context, files, scope, contexts)
    update { component }
    return component
  }

  override fun close() {
    value?.close()
  }

  override fun compareAndSet(expect: BudgetGraph?, update: BudgetGraph?): Boolean {
    val previous = value
    val successfullySet = super.compareAndSet(expect, update)
    if (successfullySet) previous?.close()
    return successfullySet
  }
}
