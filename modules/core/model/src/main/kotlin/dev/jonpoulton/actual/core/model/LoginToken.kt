package dev.jonpoulton.actual.core.model

import androidx.compose.runtime.Immutable

@Immutable
@JvmInline
value class LoginToken(private val value: String) {
  override fun toString(): String = value
}
