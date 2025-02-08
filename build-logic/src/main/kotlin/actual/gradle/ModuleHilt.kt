package actual.gradle

import blueprint.core.getLibrary
import com.autonomousapps.DependencyAnalysisPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper

class ModuleHilt : Plugin<Project> {
  override fun apply(target: Project): Unit = with(target) {
    with(pluginManager) {
      apply(KotlinAndroidPluginWrapper::class.java)
      apply(ConventionAndroidLibrary::class.java)
      apply(ConventionDiagrams::class.java)
      apply(ConventionHilt::class.java)
      apply(ConventionKotlinJvm::class.java)
      apply(ConventionStyle::class.java)
      apply(DependencyAnalysisPlugin::class.java)
      apply(ConventionSortDependencies::class.java)
    }

    val api by configurations
    val implementation by configurations

    dependencies {
      api(libs.getLibrary("dagger.core"))
      implementation(libs.getLibrary("hilt.core"))
    }
  }
}
