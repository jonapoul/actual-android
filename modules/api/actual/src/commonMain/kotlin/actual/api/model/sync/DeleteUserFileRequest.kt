package actual.api.model.sync

import actual.account.model.LoginToken
import actual.budget.model.BudgetId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserFileRequest(
  @SerialName("fileId") val id: BudgetId,
  @SerialName("token") val token: LoginToken,
)
