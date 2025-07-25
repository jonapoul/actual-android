package actual.core.connection

import actual.api.builder.ClientFactory
import actual.api.client.AccountApi
import actual.api.client.ActualApis
import actual.api.client.ActualApisStateHolder
import actual.api.client.ActualJson
import actual.api.client.BaseApi
import actual.api.client.HealthApi
import actual.api.client.MetricsApi
import actual.api.client.SyncApi
import actual.api.client.SyncDownloadApi
import actual.core.model.ServerUrl
import actual.prefs.AppGlobalPreferences
import alakazam.kotlin.core.collectFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import logcat.logcat
import okio.FileSystem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionMonitor @Inject constructor(
  private val scope: CoroutineScope,
  private val clientFactory: ClientFactory,
  private val apiStateHolder: ActualApisStateHolder,
  private val preferences: AppGlobalPreferences,
  private val fileSystem: FileSystem,
) {
  private var job: Job? = null

  fun start() {
    job?.cancel()
    job = scope.collectFlow(preferences.serverUrl.asFlow()) { url ->
      logcat.v { "ConnectionMonitor url=$url" }
      if (url == null) {
        apiStateHolder.reset()
      } else {
        // Close previous client's resources before rebuilding
        apiStateHolder.value?.close()
        tryToBuildApis(url)
      }
    }
  }

  private fun tryToBuildApis(url: ServerUrl) = try {
    val client = clientFactory.build(ActualJson)
    val apis = ActualApis(
      serverUrl = url,
      client = client,
      account = AccountApi(url, client),
      base = BaseApi(url, client),
      health = HealthApi(url, client),
      metrics = MetricsApi(url, client),
      sync = SyncApi(url, client),
      syncDownload = SyncDownloadApi(url, client, fileSystem),
    )
    apiStateHolder.update { apis }
  } catch (e: CancellationException) {
    throw e
  } catch (e: Exception) {
    logcat.w(e) { "Failed building APIs for $url" }
    apiStateHolder.reset()
  }

  fun stop() {
    job?.cancel()
    job = null
  }
}
