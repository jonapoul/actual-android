import blueprint.core.commonMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
}

kotlin {
  commonMainDependencies {
    api(libs.kotlinx.serialization.json)
    api(libs.test.ktor)
    api(projects.core.model)
    implementation(projects.api.builder)
  }
}
