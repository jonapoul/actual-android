package actual.budget.list.vm

import actual.account.model.LoginToken
import actual.api.client.ActualApis
import actual.api.client.ActualApisStateHolder
import actual.api.client.ActualJson
import actual.api.client.SyncApi
import actual.budget.model.Budget
import actual.budget.model.BudgetId
import actual.budget.model.BudgetState
import actual.core.model.Protocol
import actual.core.model.ServerUrl
import actual.prefs.KeyPreferences
import actual.test.emptyMockEngine
import actual.test.plusAssign
import actual.test.respondJson
import actual.test.testHttpClient
import alakazam.test.core.TestCoroutineContexts
import alakazam.test.core.standardDispatcher
import io.ktor.client.engine.mock.MockEngine
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import java.net.NoRouteToHostException
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BudgetListFetcherTest {
  private lateinit var budgetListFetcher: BudgetListFetcher
  private lateinit var apisStateHolder: ActualApisStateHolder
  private lateinit var mockEngine: MockEngine
  private lateinit var keyPreferences: KeyPreferences

  private fun TestScope.before() {
    mockEngine = emptyMockEngine()
    apisStateHolder = ActualApisStateHolder()
    keyPreferences = mockk { every { contains(any()) } returns false }
    budgetListFetcher = BudgetListFetcher(
      contexts = TestCoroutineContexts(standardDispatcher),
      apisStateHolder = apisStateHolder,
      keyPreferences = keyPreferences,
    )
  }

  @AfterTest
  fun after() {
    mockEngine.close()
  }

  @Test
  fun `Failure if no APIs stored`() = runTest {
    before()
    apisStateHolder.update { null }
    val result = budgetListFetcher.fetchBudgets(TOKEN)
    assertEquals(actual = result, expected = FetchBudgetsResult.NotLoggedIn)
  }

  @Test
  fun `Handle success response`() = runTest {
    before()

    // given
    mockEngine += { respondJson(VALID_RESPONSE) }
    apisStateHolder.update { buildApis() }

    // when
    val result = budgetListFetcher.fetchBudgets(TOKEN)

    // then
    assertEquals(
      actual = result,
      expected = FetchBudgetsResult.Success(
        budgets = listOf(
          Budget(
            name = "Main Budget",
            state = BudgetState.Unknown,
            encryptKeyId = "7fe20d96-ab62-43bc-b69c-53f55a26cbbf",
            groupId = "16f9c400-cdf5-43ae-983f-4dbcccb10ccf",
            cloudFileId = BudgetId("525fecc4-5080-4d01-b2ea-6032e5ee25c1"),
            hasKey = false,
          ),
        ),
      ),
    )
  }

  @Test
  fun `Handle invalid JSON format`() = runTest {
    before()

    // given
    val body = """
      {
        "status": "ok",
        "data": [ { "invalid_format": true } ]
      }
    """.trimIndent()
    mockEngine += { respondJson(body) }
    apisStateHolder.update { buildApis() }

    // when
    val result = budgetListFetcher.fetchBudgets(TOKEN)

    // then
    assertIs<FetchBudgetsResult.InvalidResponse>(result)
  }

  @Test
  fun `Handle network failure`() = runTest {
    before()

    // given
    val syncApi = mockk<SyncApi> {
      coEvery { fetchUserFiles(TOKEN) } throws NoRouteToHostException()
    }
    mockEngine += { respondJson(VALID_RESPONSE) }
    apisStateHolder.update { buildApis(syncApi) }

    // when
    val result = budgetListFetcher.fetchBudgets(TOKEN)

    // then
    assertIs<FetchBudgetsResult.NetworkFailure>(result)
  }

  @Test
  fun `Handle failure response`() = runTest {
    before()

    // given
    val responseJson = """
      {
        "status": "error",
        "reason": "something broke"
      }
    """.trimIndent()
    mockEngine += { respondJson(responseJson, HttpStatusCode.Forbidden) }
    apisStateHolder.update { buildApis() }

    // when
    val result = budgetListFetcher.fetchBudgets(TOKEN)

    // then
    assertEquals(
      expected = FetchBudgetsResult.FailureResponse(reason = "something broke"),
      actual = result,
    )
  }

  private fun buildApis(
    syncApi: SyncApi = SyncApi(SERVER_URL, testHttpClient(mockEngine, ActualJson)),
  ) = mockk<ActualApis> { every { sync } returns syncApi }

  private companion object {
    val TOKEN = LoginToken(value = "abc-123")
    val SERVER_URL = ServerUrl(Protocol.Https, "test.unused.com")

    val VALID_RESPONSE = """
      {
        "status": "ok",
        "data": [
          {
            "deleted": 0,
            "fileId": "525fecc4-5080-4d01-b2ea-6032e5ee25c1",
            "groupId": "16f9c400-cdf5-43ae-983f-4dbcccb10ccf",
            "name": "Main Budget",
            "encryptKeyId": "7fe20d96-ab62-43bc-b69c-53f55a26cbbf",
            "owner": null,
            "usersWithAccess": []
          }
        ]
      }
    """.trimIndent()
  }
}
