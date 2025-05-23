package actual.api.model.account

import actual.account.model.LoginMethod
import actual.account.model.Password
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
  @SerialName("loginMethod") val loginMethod: LoginMethod,
  @SerialName("password") val password: Password,
)
