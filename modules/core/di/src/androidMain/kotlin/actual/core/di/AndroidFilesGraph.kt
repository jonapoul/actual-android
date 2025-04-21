package actual.core.di

import actual.core.files.AndroidAssets
import actual.core.files.AndroidFileSystem
import actual.core.files.Assets
import actual.core.files.FileSystem
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface AndroidFilesGraph : FilesGraph {
  @Binds val AndroidAssets.bind: Assets
  @Binds val AndroidFileSystem.bind: FileSystem
}
