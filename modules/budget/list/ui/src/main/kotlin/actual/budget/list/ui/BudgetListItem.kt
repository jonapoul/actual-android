package actual.budget.list.ui

import actual.budget.model.Budget
import actual.budget.model.BudgetState
import actual.core.icons.ActualIcons
import actual.core.icons.Key
import actual.core.ui.ActualFontFamily
import actual.core.ui.BareIconButton
import actual.core.ui.LocalTheme
import actual.core.ui.PreviewColumn
import actual.core.ui.RowShape
import actual.core.ui.Theme
import actual.l10n.Strings
import alakazam.android.ui.compose.HorizontalSpacer
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * actual/packages/desktop-client/src/components/manager/BudgetList.tsx
 */
@Composable
internal fun BudgetListItem(
  budget: Budget,
  onClickOpen: () -> Unit,
  onClickDelete: () -> Unit,
  modifier: Modifier = Modifier,
  theme: Theme = LocalTheme.current,
) {
  Row(
    modifier = modifier
      .background(theme.budgetItemBackground, RowShape)
      .border(1.dp, theme.buttonNormalBorder, RowShape)
      .clickable(onClick = onClickOpen)
      .padding(horizontal = 15.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val description = budgetDescription(budget)

    Column(
      modifier = Modifier.weight(1f),
    ) {
      Text(
        text = budget.name,
        fontSize = 16.sp,
        fontFamily = ActualFontFamily,
        fontWeight = FontWeight.W700,
        color = theme.budgetItemTextPrimary,
      )

      BudgetStateText(
        state = budget.state,
        theme = theme,
      )

      Text(
        modifier = Modifier.padding(top = 4.dp),
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = theme.budgetItemTextSecondary,
        fontSize = 10.sp,
        lineHeight = 12.sp,
      )
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      if (budget.encryptKeyId != null) {
        Icon(
          modifier = Modifier
            .padding(10.dp)
            .size(13.dp),
          imageVector = ActualIcons.Key,
          contentDescription = description,
          tint = if (budget.hasKey) theme.formLabelText else theme.buttonNormalDisabledText,
        )

        HorizontalSpacer(8.dp)
      }

      var showDeleteMenu by remember { mutableStateOf(false) }

      BareIconButton(
        imageVector = Icons.Filled.MoreVert,
        contentDescription = Strings.navBack,
        onClick = { showDeleteMenu = true },
      )

      DeleteMenu(
        expanded = showDeleteMenu,
        onDismiss = { showDeleteMenu = false },
        onClickDelete = onClickDelete,
      )
    }
  }
}

@Composable
private fun DeleteMenu(
  expanded: Boolean,
  onDismiss: () -> Unit,
  onClickDelete: () -> Unit,
) {
  DropdownMenu(
    expanded = expanded,
    onDismissRequest = onDismiss,
  ) {
    val deleteText = Strings.budgetDelete
    DropdownMenuItem(
      text = { Text(deleteText, fontFamily = ActualFontFamily) },
      leadingIcon = { Icon(imageVector = Icons.Filled.DeleteForever, contentDescription = deleteText) },
      onClick = {
        onDismiss()
        onClickDelete()
      },
    )
  }
}

@Composable
@ReadOnlyComposable
private fun budgetDescription(budget: Budget) = when {
  budget.state == BudgetState.Unknown -> Strings.listBudgetsOffline
  budget.hasKey -> Strings.listBudgetsEncryptedWithKey
  budget.encryptKeyId != null -> Strings.listBudgetsEncryptedWithoutKey
  else -> Strings.listBudgetsUnencrypted
}

@Preview
@Composable
private fun Synced() = PreviewColumn {
  BudgetListItem(
    modifier = Modifier.fillMaxWidth(),
    budget = PreviewBudgetSynced,
    onClickOpen = {},
    onClickDelete = {},
  )
}

@Preview
@Composable
private fun SyncedThinner() = PreviewColumn {
  BudgetListItem(
    modifier = Modifier.width(300.dp),
    budget = PreviewBudgetSynced,
    onClickOpen = {},
    onClickDelete = {},
  )
}

@Preview
@Composable
private fun Warning() = PreviewColumn {
  BudgetListItem(
    modifier = Modifier.fillMaxWidth(),
    budget = PreviewBudgetSynced.copy(state = BudgetState.Broken, encryptKeyId = null),
    onClickOpen = {},
    onClickDelete = {},
  )
}
