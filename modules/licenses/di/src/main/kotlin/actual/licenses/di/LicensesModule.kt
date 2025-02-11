package actual.licenses.di

import actual.licenses.data.AndroidAssetsProvider
import actual.licenses.data.AssetsProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LicensesModule {
  @Binds
  fun assets(impl: AndroidAssetsProvider): AssetsProvider
}
