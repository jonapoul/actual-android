package actual.core.di

import actual.account.model.Password
import actual.core.model.ServerUrl
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides

@BindingContainer
class DebugContainer(
  @get:Provides val defaultPassword: Password.Provider,
  @get:Provides val defaultServerUrl: ServerUrl.Provider,
)
