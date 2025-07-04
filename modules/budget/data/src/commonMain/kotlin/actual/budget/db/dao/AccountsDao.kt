package actual.budget.db.dao

import actual.budget.db.Accounts
import actual.budget.db.BudgetDatabase
import actual.budget.db.withResult
import actual.budget.model.AccountId
import alakazam.kotlin.core.CoroutineContexts
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow

class AccountsDao(database: BudgetDatabase, private val contexts: CoroutineContexts) {
  private val queries = database.accountsQueries

  fun observe(id: AccountId): Flow<Accounts?> = queries
    .getById(id)
    .asFlow()
    .mapToOneOrNull(contexts.default)

  suspend operator fun get(id: AccountId): Accounts? = queries.withResult { getById(id).executeAsOneOrNull() }
}
