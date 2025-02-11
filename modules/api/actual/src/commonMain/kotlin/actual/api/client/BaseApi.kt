package actual.api.client

import actual.api.model.base.InfoResponse
import retrofit2.http.GET

interface BaseApi {
  @GET("info")
  suspend fun info(): InfoResponse
}
