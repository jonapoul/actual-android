@file:Suppress("USELESS_ELVIS", "UNNECESSARY_SAFE_CALL")

package actual.android.app

import actual.account.model.Password
import actual.core.di.AndroidAppGraph
import actual.core.di.DebugContainer
import actual.core.model.ServerUrl
import alakazam.kotlin.core.BuildConfig
import android.content.Context
import dev.zacsweers.metro.createGraphFactory
import actual.android.app.BuildConfig as ActualBuildConfig

internal fun setUpDi(
  buildConfig: BuildConfig,
  context: Context,
): AndroidAppGraph {
  val factory = createGraphFactory<AndroidAppGraph.Factory>()
  val debug = DebugContainer(
    defaultPassword = { ActualBuildConfig.DEFAULT_PASSWORD?.let(::Password) ?: Password.Empty },
    defaultServerUrl = { ActualBuildConfig.DEFAULT_URL?.let(::ServerUrl) },
  )
  return factory.create(
    context = context,
    buildConfig = buildConfig,
    debug = debug,
  )
}
