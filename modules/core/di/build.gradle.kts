import blueprint.core.androidMainDependencies
import blueprint.core.commonMainDependencies

plugins {
  alias(libs.plugins.module.metro)
}

kotlin {
  commonMainDependencies {
    api(projects.core.files)
    api(libs.alakazam.kotlin.core)
    api(libs.preferences.core)
  }

  androidMainDependencies {
    api(libs.alakazam.android.core)
    implementation(libs.preferences.android)
  }
}
