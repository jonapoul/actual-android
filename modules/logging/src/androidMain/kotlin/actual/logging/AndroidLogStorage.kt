package actual.logging

import android.content.Context
import okio.Path
import okio.Path.Companion.toOkioPath

class AndroidLogStorage(private val context: Context) : LogStorage {
  override fun directory(): Path = context.filesDir.resolve("../logs").toOkioPath()
}
