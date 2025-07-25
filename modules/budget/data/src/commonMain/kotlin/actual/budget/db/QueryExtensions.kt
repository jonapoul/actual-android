package actual.budget.db

import app.cash.sqldelight.SuspendingTransacter

suspend fun <T : SuspendingTransacter> T.withoutResult(
  query: suspend T.() -> Unit,
): Unit = transaction { query() }

suspend fun <T : SuspendingTransacter, R> T.withResult(
  query: suspend T.() -> R,
): R = transactionWithResult { query() }
