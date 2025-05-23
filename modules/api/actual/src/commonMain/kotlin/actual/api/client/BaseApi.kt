package actual.api.client

import actual.api.model.base.InfoResponse
import actual.codegen.GET
import actual.codegen.KtorApi
import actual.core.model.ServerUrl
import io.ktor.client.HttpClient

@KtorApi
interface BaseApi {
  @GET("/info")
  suspend fun fetchInfo(): InfoResponse
}

expect fun BaseApi(serverUrl: ServerUrl, client: HttpClient): BaseApi
