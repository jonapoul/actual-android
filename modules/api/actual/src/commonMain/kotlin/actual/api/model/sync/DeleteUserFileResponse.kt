package actual.api.model.sync

import actual.api.model.account.FailureReason
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface DeleteUserFileResponse {
  @Serializable
  data class Success(
    @SerialName("status") val status: String,
  ) : DeleteUserFileResponse

  @Serializable
  data class Failure(
    @SerialName("status") val status: String,
    @SerialName("reason") val reason: FailureReason,
    @SerialName("details") val details: String?,
  ) : DeleteUserFileResponse
}
