package org.example.mandm.commonComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import org.example.mandm.dataModel.CustomerEntity

@Composable
fun CustomerSearchField(
    modifier: Modifier = Modifier,
    query: String,
    results: List<CustomerEntity>,
    onQueryChange: (String) -> Unit,
    onSelect: (CustomerEntity) -> Unit,
    onMoveFocusNext: (() -> Unit)? = null
) {
    var hasFocus by remember { mutableStateOf(false) }
    val focusManager: FocusManager = LocalFocusManager.current

    val density = LocalDensity.current
    var anchorWidth by remember { mutableStateOf(0f) }

    Box(modifier = modifier) {
        Column(Modifier.fillMaxWidth()) {
            ValidatedInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { hasFocus = it.isFocused }
                    .onGloballyPositioned { coords ->
                        anchorWidth = coords.boundsInWindow().width
                    },
                hintText = "Search by Name,Id or Number",
                initialValue = query,
                onValidationChanged = { _, v -> onQueryChange(v) }
            )
        }

        val desiredWidthDp = with(density) { (anchorWidth).toDp() - 16.dp }
        DropdownMenu(
            expanded = hasFocus,
            onDismissRequest = { /* keep open while focused */ },
            modifier = Modifier.width(desiredWidthDp.coerceAtLeast(220.dp)),
            offset = DpOffset(0.dp, 4.dp),
            properties = PopupProperties(focusable = false)
        ) {
            if (results.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No users found", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)) },
                    onClick = {},
                    enabled = false
                )
            }
            results.forEach { c ->
                DropdownMenuItem(
                    text = {
                        // Match UsersListItem layout
                        Column(Modifier.fillMaxWidth()) {
                            Row(Modifier.fillMaxWidth()) {
                                Column(Modifier.weight(1f)) {
                                    Text(text = c.userName, style = MaterialTheme.typography.bodyMedium)
                                    Text(text = c.nickName ?: "------", style = MaterialTheme.typography.labelMedium)
                                }
                                Text(text = c.village, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    },
                    onClick = {
                        onSelect(c)
                        focusManager.clearFocus(force = true)
                        onMoveFocusNext?.invoke()
                    }
                )
            }
        }
    }
}


