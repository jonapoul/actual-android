package actual.core.di

import actual.account.model.Password
import actual.core.model.ServerUrl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides

@BindingContainer
class DebugContainer(
  private val defaultPassword: Password.Provider,
  private val defaultServerUrl: ServerUrl.Provider,
) {
  @Provides fun password(): Password.Provider = defaultPassword
  @Provides fun serverUrl(): ServerUrl.Provider = defaultServerUrl
}
