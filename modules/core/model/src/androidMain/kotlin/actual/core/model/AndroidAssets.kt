package actual.core.model

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import java.io.InputStream

@Inject
@ContributesBinding(AppScope::class)
class AndroidAssets(private val context: Context) : Assets {
  override fun getStream(name: String): InputStream = context.assets.open(name)
}
