package actual.licenses.data

import kotlinx.serialization.json.Json

internal val LicensesJson = Json {
  ignoreUnknownKeys = true
}
