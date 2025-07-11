package actual.test

import actual.budget.db.BudgetDatabase
import actual.budget.db.JvmSqlDriverFactory
import actual.budget.db.SqlDriverFactory
import actual.budget.db.buildDatabase
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import java.io.File

fun inMemoryDriverFactory() = JvmSqlDriverFactory(JdbcSqliteDriver.IN_MEMORY)

fun fileDriverFactory(file: File) = JvmSqlDriverFactory(url = "jdbc:sqlite:${file.absolutePath}")

fun runDatabaseTest(
  driverFactory: SqlDriverFactory = inMemoryDriverFactory(),
  action: suspend BudgetDatabase.(TestScope) -> Unit,
) = runTest {
  val driver = driverFactory.create()
  val database = buildDatabase(driver)
  driver.use { action(database, this) }
}
