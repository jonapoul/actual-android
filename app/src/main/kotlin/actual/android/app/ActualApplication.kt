package actual.android.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject
import actual.core.config.BuildConfig as ActualBuildConfig

@HiltAndroidApp
class ActualApplication : Application() {
  @Inject
  lateinit var bc: ActualBuildConfig

  override fun onCreate() {
    super.onCreate()
    Timber.plant(ActualTree())

    Timber.i("onCreate")
    Timber.d("name=${bc.versionName} code=${bc.versionCode} time=${bc.buildTime}")
    Timber.d("manufacturer=${bc.manufacturer} model=${bc.model} os=${bc.os} platform=${bc.platform}")
  }
}
