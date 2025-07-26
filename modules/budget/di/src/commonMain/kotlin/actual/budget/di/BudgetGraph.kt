@file:Suppress("ktlint:standard:parameter-list-wrapping")

package actual.budget.di

import actual.budget.db.BudgetDatabase
import actual.budget.model.BudgetFiles
import actual.budget.model.BudgetId
import actual.budget.model.BudgetScope
import actual.budget.model.DbMetadata
import actual.budget.prefs.BudgetLocalPreferences
import alakazam.kotlin.core.CoroutineContexts
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory
import kotlinx.coroutines.CoroutineScope

@DependencyGraph(BudgetScope::class)
interface BudgetGraph : AutoCloseable {
  val driver: SqlDriver
  val database: BudgetDatabase
  val localPreferences: BudgetLocalPreferences

  val budgetId: BudgetId get() = localPreferences.value.cloudFileId

  override fun close() {
    driver.close()
  }

  @DependencyGraph.Factory
  fun interface Factory {
    fun create(
      @Provides files: BudgetFiles,
      @Provides metadata: DbMetadata,
      @Provides scope: CoroutineScope,
      @Provides contexts: CoroutineContexts,
    ): BudgetGraph
  }

  companion object {
    fun build(
      metadata: DbMetadata,
      context: Context,
      files: BudgetFiles,
      scope: CoroutineScope,
      contexts: CoroutineContexts,
    ): BudgetGraph = createGraphFactory<Factory>().create(
      files = files,
      metadata = metadata,
      scope = scope,
      contexts = contexts,
    )
  }
}

fun BudgetGraph.throwIfWrongBudget(expected: BudgetId) {
  require(budgetId == expected) {
    "Loading from the wrong budget! Expected $expected, got $budgetId"
  }
}
