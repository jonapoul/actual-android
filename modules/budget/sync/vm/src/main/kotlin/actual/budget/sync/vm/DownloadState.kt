package actual.budget.sync.vm

import java.io.File

sealed interface DownloadState {
  val total: Bytes

  data class InProgress(
    val read: Bytes,
    override val total: Bytes,
  ) : DownloadState

  data class Done(
    val file: File,
    override val total: Bytes,
  ) : DownloadState

  data class Failed(
    val cause: String,
    override val total: Bytes,
  ) : DownloadState
}
