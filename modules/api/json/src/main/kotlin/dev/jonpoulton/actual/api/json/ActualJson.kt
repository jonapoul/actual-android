package dev.jonpoulton.actual.api.json

import kotlinx.serialization.json.Json

val ActualJson = Json {
  encodeDefaults = true
}
