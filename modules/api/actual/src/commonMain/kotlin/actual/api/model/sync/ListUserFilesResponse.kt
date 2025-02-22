package actual.api.model.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface ListUserFilesResponse {
  @Serializable
  data class Success(
    @SerialName("data") val data: List<Item>,
  ) : ListUserFilesResponse

  @Serializable
  data class Failure(
    @SerialName("reason") val reason: String,
  ) : ListUserFilesResponse

  @Serializable
  data class Item(
    @SerialName("deleted") val deleted: Int,
    @SerialName("fileId") val fileId: String,
    @SerialName("groupId") val groupId: String,
    @SerialName("name") val name: String,
    @SerialName("encryptKeyId") val encryptKeyId: String?,
    @SerialName("owner") val owner: String?,
    @SerialName("usersWithAccess") val usersWithAccess: List<UserWithAccess>,
  )

  @Serializable
  data class UserWithAccess(
    @SerialName("userId") val userId: String,
    @SerialName("userName") val userName: String,
    @SerialName("displayName") val displayName: String,
    @SerialName("owner") val isOwner: Boolean,
  )
}
