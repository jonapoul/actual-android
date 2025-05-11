package actual.account.login.domain

import actual.account.model.LoginMethod
import actual.account.model.Password
import actual.api.client.ActualApisStateHolder
import actual.api.model.account.LoginRequest
import actual.api.model.account.LoginResponse
import alakazam.kotlin.core.CoroutineContexts
import alakazam.kotlin.core.requireMessage
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class LoginRequester @Inject internal constructor(
  private val contexts: CoroutineContexts,
  private val apisStateHolder: ActualApisStateHolder,
  private val loginPreferences: LoginPreferences,
) {
  suspend fun logIn(password: Password): LoginResult {
    val apis = apisStateHolder.value ?: return LoginResult.OtherFailure("URL not configured")

    // TODO: handle other login methods
    val request = LoginRequest(LoginMethod.Password, password)
    val response = try {
      withContext(contexts.io) { apis.account.login(request) }
    } catch (e: CancellationException) {
      throw e
    } catch (e: ResponseException) {
      return with(e.response.status) { LoginResult.HttpFailure(value, description) }
    } catch (e: Exception) {
      return when (e) {
        is IOException -> LoginResult.NetworkFailure(e.requireMessage())
        else -> LoginResult.OtherFailure(e.requireMessage())
      }
    }

    val result = when (val data = response.data) {
      is LoginResponse.Data.Invalid -> LoginResult.InvalidPassword
      is LoginResponse.Data.Valid -> LoginResult.Success(data.token)
    }

    // Also save the token
    loginPreferences.token.set(response.data.token)

    return result
  }
}
