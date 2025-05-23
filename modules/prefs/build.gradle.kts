import blueprint.core.androidUnitTestDependencies
import blueprint.core.commonMainDependencies

plugins {
  alias(libs.plugins.module.multiplatform)
}

kotlin {
  commonMainDependencies {
    api(libs.javaxInject)
    api(libs.preferences.core)
    api(projects.account.model)
    api(projects.core.model)
  }

  androidUnitTestDependencies {
    implementation(projects.test.buildconfig)
    implementation(projects.test.coroutines)
    implementation(projects.test.prefs)
  }
}
