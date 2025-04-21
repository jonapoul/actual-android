package actual.core.di

import actual.core.files.Assets
import actual.core.files.FileSystem

interface FilesGraph {
  val assets: Assets
  val fileSystem: FileSystem
}
