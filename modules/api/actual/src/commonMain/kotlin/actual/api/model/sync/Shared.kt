package actual.api.model.sync

import actual.budget.model.BudgetId
import actual.core.model.Base64String
import actual.core.model.KeyId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserFile(
  @SerialName("deleted") val deleted: Int,
  @SerialName("fileId") val fileId: BudgetId,
  @SerialName("groupId") val groupId: String,
  @SerialName("name") val name: String,
  @SerialName("encryptKeyId") val encryptKeyId: KeyId? = null,
  @SerialName("owner") val owner: String? = null,
  @SerialName("encryptMeta") val encryptMeta: EncryptMeta? = null,
  @SerialName("usersWithAccess") val usersWithAccess: List<UserWithAccess> = emptyList(),
)

@Serializable
data class UserWithAccess(
  @SerialName("userId") val userId: String,
  @SerialName("userName") val userName: String,
  @SerialName("displayName") val displayName: String,
  @SerialName("owner") val isOwner: Boolean,
)

@Serializable
data class EncryptMeta(
  @SerialName("keyId") val keyId: KeyId?,
  @SerialName("algorithm") val algorithm: String,
  @SerialName("iv") val iv: Base64String,
  @SerialName("authTag") val authTag: Base64String,
)
