package actual.db

import actual.budget.model.BudgetId
import actual.core.files.AndroidDatabaseDirectory
import alakazam.test.core.getResourceAsStream
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
class LoadExistingDatabaseFromFile {
  private lateinit var context: Context
  private lateinit var driver: SqlDriver

  @BeforeTest
  fun before() {
    context = ApplicationProvider.getApplicationContext()
  }

  @AfterTest
  fun after() {
    driver.close()
  }

  @Test
  fun `Opening existing file and reading table data`() = runTest {
    val file = loadDatabaseIntoFile()
    driver = AndroidSqlDriverFactory(BUDGET_ID, context).create()
    val db = buildDatabase(driver)

    val viewHash = db.metaQueries.withResult {
      getValue(key = "view-hash")
        .executeAsOneOrNull()
        ?.value_
    }

    assertEquals(expected = "c379fa428efd55a684aba4947ad054e0", actual = viewHash)
    assertTrue(file.exists())
  }

  private fun loadDatabaseIntoFile(): File {
    val path = AndroidDatabaseDirectory(context).pathFor(BUDGET_ID)
    val file = path.toFile()
    getResourceAsStream("test-db.sqlite").use { input ->
      file.outputStream().use { output ->
        input.copyTo(output)
      }
    }
    return file
  }

  private companion object {
    val BUDGET_ID = BudgetId("abc-123")
  }
}
