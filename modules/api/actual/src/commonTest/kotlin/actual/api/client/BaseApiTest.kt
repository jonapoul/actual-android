package actual.api.client

import actual.api.model.base.Build
import actual.api.model.base.InfoResponse
import actual.test.BaseResponses
import actual.test.emptyMockEngine
import actual.test.latestRequestHeaders
import actual.test.latestRequestUrl
import actual.test.plusAssign
import actual.test.respondJson
import actual.test.testHttpClient
import alakazam.test.core.assertThrows
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BaseApiTest {
  private lateinit var mockEngine: MockEngine
  private lateinit var baseApi: BaseApi

  @BeforeTest
  fun before() {
    mockEngine = emptyMockEngine()
    baseApi = BaseApi(SERVER_URL, testHttpClient(mockEngine, ActualJson))
  }

  @AfterTest
  fun after() {
    mockEngine.close()
  }

  @Test
  fun `Fetch info request format`() = runTest {
    mockEngine += { respondJson(BaseResponses.INFO_SUCCESS_200) }
    baseApi.fetchInfo()

    assertEquals(
      actual = mockEngine.latestRequestUrl(),
      expected = "https://test.server.com/info",
    )

    assertEquals(
      actual = mockEngine.latestRequestHeaders(),
      expected = mapOf(
        "Accept" to listOf("application/json"),
        "Accept-Charset" to listOf("UTF-8"),
      ),
    )
  }

  @Test
  fun `Fetch info success response`() = runTest {
    // given
    mockEngine += { respondJson(BaseResponses.INFO_SUCCESS_200) }

    // when
    val response = baseApi.fetchInfo()

    // then
    assertEquals(
      actual = response,
      expected = InfoResponse(
        build = Build(
          name = "@actual-app/sync-server",
          description = "actual syncing server",
          version = "25.7.1",
        ),
      ),
    )
  }

  @Test
  fun `Fetch info failure response`() = runTest {
    // given
    mockEngine += { respondError(HttpStatusCode.BadRequest) }

    // when
    assertThrows<ClientRequestException> { baseApi.fetchInfo() }
  }
}
