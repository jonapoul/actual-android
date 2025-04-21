package actual.core.files

import android.content.Context
import dev.zacsweers.metro.Inject
import java.io.InputStream

class AndroidAssets @Inject constructor(private val context: Context) : Assets {
  override fun getStream(name: String): InputStream = context.assets.open(name)
}
