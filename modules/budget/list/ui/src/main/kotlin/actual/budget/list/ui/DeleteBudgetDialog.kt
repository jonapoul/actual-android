package actual.budget.list.ui

import actual.budget.list.res.BudgetListStrings
import actual.budget.list.vm.Budget
import actual.core.ui.ActualAlertDialog
import actual.core.ui.ActualFontFamily
import actual.core.ui.BareActualTextButton
import actual.core.ui.PreviewActualColumn
import actual.core.ui.Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
internal fun DeleteBudgetDialog(
  budget: Budget,
  onDeleteLocal: () -> Unit,
  onDeleteRemote: () -> Unit,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  ActualAlertDialog(
    modifier = modifier,
    title = BudgetListStrings.budgetDeleteDialogTitle(budget.name),
    onDismissRequest = onDismiss,
    buttons = {
      TextButton(onClick = onDismiss) {
        Text(
          text = BudgetListStrings.budgetDeleteDialogDismiss,
          fontFamily = ActualFontFamily,
        )
      }
    },
    content = {
      Content(
        onDeleteLocal = {
          onDeleteLocal()
          onDismiss()
        },
        onDeleteRemote = {
          onDeleteRemote()
          onDismiss()
        },
      )
    },
  )
}

@Stable
@Composable
private fun Content(
  onDeleteLocal: () -> Unit,
  onDeleteRemote: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = annotatedString(),
      fontSize = 14.sp,
    )

    BareActualTextButton(
      text = BudgetListStrings.budgetDeleteDialogHostedButton,
      colors = { theme, pressed -> theme.errorPrimary(pressed) },
      onClick = onDeleteRemote,
      isEnabled = false,
    )

    Text(
      text = BudgetListStrings.budgetDeleteDialogLocalTxt,
      fontSize = 14.sp,
    )

    BareActualTextButton(
      text = BudgetListStrings.budgetDeleteDialogLocalButton,
      colors = { theme, pressed -> theme.errorBare(pressed) },
      onClick = onDeleteLocal,
    )
  }
}

@Stable
@Composable
fun annotatedString() = buildAnnotatedString {
  append(BudgetListStrings.budgetDeleteDialogHostedTxt1)
  append(" ")
  withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
    append(BudgetListStrings.budgetDeleteDialogHostedTxt2)
  }
  append(" ")
  append(BudgetListStrings.budgetDeleteDialogHostedTxt3)
}

@Stable
@Composable
private fun Theme.errorPrimary(isPressed: Boolean) = ButtonDefaults.buttonColors(
  containerColor = if (isPressed) buttonPrimaryBackground else errorBackground,
  contentColor = if (isPressed) buttonPrimaryText else errorText,
)

@Stable
@Composable
private fun Theme.errorBare(isPressed: Boolean) = ButtonDefaults.outlinedButtonColors(
  containerColor = if (isPressed) buttonBareBackground else buttonBareBackgroundHover,
  contentColor = if (isPressed) buttonBareText else errorText,
)

@Preview
@Composable
private fun PreviewContent() = PreviewActualColumn {
  Content(
    onDeleteLocal = {},
    onDeleteRemote = {},
  )
}
