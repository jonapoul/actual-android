package actual.android.app

import actual.core.di.AndroidAppGraph
import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.Intent
import androidx.core.app.AppComponentFactory
import dev.zacsweers.metro.Provider
import kotlin.reflect.KClass

/**
 * From https://github.com/ZacSweers/metro/blob/main/samples/compose-navigation-app/src/main/kotlin/dev/zacsweers/metro/sample/androidviewmodel/components/MetroAppComponentFactory.kt
 */
class ActualAppComponentFactory : AppComponentFactory() {
  private inline fun <reified T : Any> getInstance(
    cl: ClassLoader,
    className: String,
    providers: Map<KClass<out T>, Provider<T>>,
  ): T? {
    val clazz = Class.forName(className, false, cl).asSubclass(T::class.java)
    val modelProvider = providers[clazz.kotlin] ?: return null
    return modelProvider()
  }

  override fun instantiateActivityCompat(
    cl: ClassLoader,
    className: String,
    intent: Intent?,
  ): Activity = getInstance(cl, className, activityProviders)
    ?: super.instantiateActivityCompat(cl, className, intent)

  override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
    super.instantiateApplicationCompat(cl, className).also { app ->
      activityProviders = (app as AndroidAppGraph.Holder).graph().activityProviders
    }

  override fun instantiateReceiverCompat(
    cl: ClassLoader,
    className: String,
    intent: Intent?,
  ): BroadcastReceiver = TODO("Not currently used")

  override fun instantiateServiceCompat(
    cl: ClassLoader,
    className: String,
    intent: Intent?,
  ): Service = TODO("Not currently used")

  override fun instantiateProviderCompat(
    cl: ClassLoader,
    className: String,
  ): ContentProvider = TODO("Not currently used")

  // AppComponentFactory can be created multiple times
  companion object {
    private lateinit var activityProviders: Map<KClass<out Activity>, Provider<Activity>>
  }
}
