package actual.budget.di

import actual.budget.db.AndroidSqlDriverFactory
import actual.budget.db.BudgetDatabase
import actual.budget.db.SqlDriverFactory
import actual.budget.db.buildDatabase
import actual.budget.model.BudgetFiles
import actual.budget.model.BudgetScope
import actual.budget.model.DbMetadata
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@BindingContainer
@ContributesTo(BudgetScope::class)
internal object BudgetDatabaseContainer {
  @Provides
  fun factory(
    metadata: DbMetadata,
    context: Context,
    files: BudgetFiles,
  ): SqlDriverFactory = AndroidSqlDriverFactory(metadata.cloudFileId, context, files)

  @Provides
  fun driver(factory: SqlDriverFactory): SqlDriver = factory.create()

  @Provides
  fun database(driver: SqlDriver): BudgetDatabase = buildDatabase(driver)
}
