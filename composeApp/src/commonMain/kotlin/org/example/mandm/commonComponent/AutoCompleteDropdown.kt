package org.example.mandm.commonComponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun <T> AutoCompleteDropdown(
    items: List<T>,
    query: String,
    onQueryChange: (String) -> Unit,
    itemContent: @Composable (T) -> Unit,   // 👈 You pass how each row looks
    onItemSelected: (T) -> Unit,
    showAddButton: Boolean = false,
    onAddClicked: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredItems = remember(query, items) {
        if (query.length >= 1) {
            items.filter { item ->
                item.toString().contains(query, ignoreCase = true)
            }
        } else emptyList()
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                onQueryChange(it)
                expanded = it.length >= 1
            },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded && (filteredItems.isNotEmpty() || showAddButton),
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            filteredItems.forEach { item ->
                DropdownMenuItem(
                    text = { itemContent(item)},
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }

            if (showAddButton && query.isNotBlank() && onAddClicked != null) {
                DropdownMenuItem(
                    text = { Text("➕ Add \"$query\"")},
                    onClick = {
                        onAddClicked(query)
                        expanded = false
                    }
                )
            }
        }
    }
}
