package actual.api.model.base

import dev.drewhamilton.poko.Poko
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Poko class InfoResponse(
  @SerialName("build") val build: Build,
)

@Serializable
@Poko class Build(
  @SerialName("name") val name: String,
  @SerialName("description") val description: String,
  @SerialName("version") val version: String,
)
