package actual.api.client

import actual.api.model.account.FailureReason
import actual.api.model.sync.EncryptMeta
import actual.api.model.sync.GetUserFileInfoResponse
import actual.api.model.sync.GetUserKeyRequest
import actual.api.model.sync.GetUserKeyResponse
import actual.api.model.sync.ListUserFilesResponse
import actual.api.model.sync.UserFile
import actual.api.model.sync.UserWithAccess
import actual.budget.model.BudgetId
import actual.core.model.KeyId
import actual.core.model.base64
import actual.test.SyncResponses
import actual.test.emptyMockEngine
import actual.test.latestRequestHeaders
import actual.test.plusAssign
import actual.test.respondJson
import actual.test.testHttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.util.toMap
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.fail

class SyncApiTest {
  private lateinit var mockEngine: MockEngine
  private lateinit var syncApi: SyncApi

  @BeforeTest
  fun before() {
    mockEngine = emptyMockEngine()
    syncApi = SyncApi(SERVER_URL, testHttpClient(mockEngine, ActualJson))
  }

  @AfterTest
  fun after() {
    mockEngine.close()
  }

  @Test
  fun `Fetch user files request headers`() = runTest {
    mockEngine += { respondJson(SyncResponses.LIST_USER_FILES_SUCCESS_200) }
    syncApi.fetchUserFiles(TOKEN)
    assertEquals(
      actual = mockEngine.latestRequestHeaders(),
      expected = mapOf(
        "X-ACTUAL-TOKEN" to listOf("abc-123"),
        "Accept" to listOf("application/json"),
        "Accept-Charset" to listOf("UTF-8"),
      ),
    )
  }

  @Test
  fun `Fetch user file info request headers`() = runTest {
    mockEngine += { respondJson(SyncResponses.GET_USER_FILE_INFO_SUCCESS_200) }
    syncApi.fetchUserFileInfo(TOKEN, BUDGET_ID)
    assertEquals(
      actual = mockEngine.latestRequestHeaders(),
      expected = mapOf(
        "X-ACTUAL-TOKEN" to listOf("abc-123"),
        "X-ACTUAL-FILE-ID" to listOf("xyz-789"),
        "Accept" to listOf("application/json"),
        "Accept-Charset" to listOf("UTF-8"),
      ),
    )
  }

  @Test
  fun `Fetch user key request headers`() = runTest {
    mockEngine += { respondJson(SyncResponses.USER_GET_KEY_SUCCESS_200) }
    val body = GetUserKeyRequest(BUDGET_ID, TOKEN)
    syncApi.fetchUserKey(body)
    val request = mockEngine.requestHistory.last()
    assertEquals(
      actual = request.headers.toMap(),
      expected = mapOf(
        "Accept" to listOf("application/json"),
        "Accept-Charset" to listOf("UTF-8"),
      ),
    )
    val actualBody = assertIs<TextContent>(request.body)
    assertEquals(actual = actualBody.text, expected = """{"fileId":"xyz-789","token":"abc-123"}""")
    assertEquals(actual = actualBody.contentType, expected = ContentType.Application.Json)
  }

  @Test
  fun `Fetch user files success response`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.LIST_USER_FILES_SUCCESS_200) }

    // when
    val response = syncApi.fetchUserFiles(TOKEN)

    // then
    val user = UserWithAccess(
      userId = "b3166dc3-0eda-42ca-83e1-b1ccce06a60b",
      displayName = "",
      userName = "",
      isOwner = true,
    )
    assertEquals(
      actual = response,
      expected = ListUserFilesResponse.Success(
        data = listOf(
          UserFile(
            deleted = 0,
            fileId = BudgetId("b328186c-c919-4333-959b-04e676c1ee46"),
            groupId = "afb25fc0-a294-4f71-ae8f-ce1e3a8fec10",
            name = "Main Budget",
            encryptKeyId = KeyId("b98636ab-200f-46be-9763-2af439aa40cb"),
          ),
          UserFile(
            deleted = 0,
            fileId = BudgetId("0e186798-305c-40f9-b609-589ffa138499"),
            groupId = "2d41985d-064f-461e-8432-23194354ee16",
            name = "Test Budget",
            owner = user.userId,
            usersWithAccess = listOf(user),
          ),
          UserFile(
            deleted = 0,
            fileId = BudgetId("81dba092-7c25-479b-bf03-854f9a289fe8"),
            groupId = "cd47aa40-0b6f-462d-a987-22e1a70e6815",
            name = "My Finances",
            encryptKeyId = KeyId("a57fc3bf-fa98-44f2-82b7-54b4755703a9"),
            owner = user.userId,
            usersWithAccess = listOf(user),
          ),
        ),
      ),
    )
  }

  @Test
  fun `Fetch user files failure response`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.LIST_USER_FILES_UNAUTHORIZED_401, HttpStatusCode.Unauthorized) }

    // when
    try {
      syncApi.fetchUserFiles(TOKEN)
      fail("Should have thrown!")
    } catch (e: ClientRequestException) {
      // Then
      assertEquals(
        actual = e.response.body<ListUserFilesResponse.Failure>(),
        expected = ListUserFilesResponse.Failure(reason = FailureReason.Unauthorized, details = "token-not-found"),
      )
    }
  }

  @Test
  fun `Get user key success`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.USER_GET_KEY_SUCCESS_200) }

    // when
    val body = GetUserKeyRequest(BUDGET_ID, TOKEN)
    val response = syncApi.fetchUserKey(body)

    // then
    val keyId = KeyId("b98636ab-200f-46be-9763-2af439aa40cb")
    val value = "nrhpJgUnl8lZvWxSRMIT0aTRKCOHeddlIuGPfNw0NQR/d81m/ZYRqaOjMwoQHpduSzuAivfVZZEslZihl8W" +
      "hOs7GVkdghwCjqr083G0261M464wHvQl2v5sB+l8f0/mQE2fco7zUagbA7Q=="
    assertEquals(
      actual = response,
      expected = GetUserKeyResponse.Success(
        data = GetUserKeyResponse.Data(
          id = keyId,
          salt = "PpZ/z6DD6xtjF89wxZOszZ6CkKXNDoBXdtBlIztmneE=".base64,
          test = GetUserKeyResponse.Test(
            value = value.base64,
            meta = EncryptMeta(
              keyId = keyId,
              algorithm = "aes-256-gcm",
              iv = "8tzhaLCrSFyVfzZF".base64,
              authTag = "35maee1UpzftRCks/yQjoB==".base64,
            ),
          ),
        ),
      ),
    )
  }

  @Test
  fun `Get user key failure`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.USER_GET_KEY_UNAUTHORIZED_401, HttpStatusCode.Unauthorized) }

    // when
    val response = try {
      val body = GetUserKeyRequest(BUDGET_ID, TOKEN)
      syncApi.fetchUserKey(body)
      fail("Should have thrown!")
    } catch (e: ClientRequestException) {
      e.response.body<GetUserKeyResponse.Failure>()
    }

    // then
    assertEquals(
      actual = response,
      expected = GetUserKeyResponse.Failure(reason = FailureReason.Unauthorized, details = "token-not-found"),
    )
  }

  @Test
  fun `Get file info success`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.GET_USER_FILE_INFO_SUCCESS_200) }

    // when
    val response = syncApi.fetchUserFileInfo(TOKEN, BUDGET_ID)

    // then
    assertEquals(
      actual = response,
      expected = GetUserFileInfoResponse.Success(
        data = UserFile(
          deleted = 0,
          fileId = BudgetId("b328186c-c919-4333-959b-04e676c1ee46"),
          groupId = "afb25fc0-a294-4f71-ae8f-ce1e3a8fec10",
          name = "Main Budget",
          encryptMeta = EncryptMeta(
            keyId = KeyId("b98636ab-200f-46be-9763-2af439aa40cb"),
            algorithm = "aes-256-gcm",
            iv = "8tzhaLCrSFyVfzZF".base64,
            authTag = "35maee1UpzftRCks/yQjoB==".base64,
          ),
        ),
      ),
    )
  }

  @Test
  fun `Get file info file not found failure`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.GET_USER_FILE_INFO_NO_FILE_400, HttpStatusCode.BadRequest) }

    // when
    val response = try {
      syncApi.fetchUserFileInfo(TOKEN, BUDGET_ID)
      fail("Should have thrown!")
    } catch (e: ClientRequestException) {
      e.response.body<GetUserFileInfoResponse.Failure>()
    }

    // then
    assertEquals(
      actual = response,
      expected = GetUserFileInfoResponse.Failure(FailureReason.FileNotFound),
    )
  }

  @Test
  fun `Get file info unauthorised failure`() = runTest {
    // given
    mockEngine += { respondJson(SyncResponses.GET_USER_FILE_INFO_UNAUTHORIZED_401, HttpStatusCode.Unauthorized) }

    // when
    val response = try {
      syncApi.fetchUserFileInfo(TOKEN, BUDGET_ID)
      fail("Should have thrown!")
    } catch (e: ClientRequestException) {
      e.response.body<GetUserFileInfoResponse.Failure>()
    }

    // then
    assertEquals(
      actual = response,
      expected = GetUserFileInfoResponse.Failure(FailureReason.Unauthorized, details = "token-not-found"),
    )
  }
}
