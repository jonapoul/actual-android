import blueprint.core.commonMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
  alias(libs.plugins.kotlin.serialization)
}

kotlin {
  commonMainDependencies {
    implementation(libs.alakazam.kotlin.serialization)
    implementation(libs.kotlinx.serialization.core)
  }
}
