package actual.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalInspectionMode

@Composable
@ReadOnlyComposable
fun isInPreview(): Boolean = LocalInspectionMode.current
