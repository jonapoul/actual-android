package actual.gradle

import dev.zacsweers.metro.gradle.MetroGradleSubplugin
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ConventionMetro : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(MetroGradleSubplugin::class.java)
    }

    extensions.configure<MetroPluginExtension> {
      generateAssistedFactories.set(true)
      generateHintProperties.set(true)
      enableKotlinVersionCompatibilityChecks.set(true)
      reportsDestination.set(layout.buildDirectory.dir("reports/metro"))
    }
  }
}
