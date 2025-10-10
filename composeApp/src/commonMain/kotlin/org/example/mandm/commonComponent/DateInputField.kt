package org.example.mandm.commonComponent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.mandm.ColorsUtils
import org.example.mandm.compactInputPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(
    modifier: Modifier = Modifier,
    title: String = "Date",
    hintText: String = "Select date",
    initialDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    onDateChanged: (LocalDate) -> Unit = {},
    onValidationChanged: (Boolean, LocalDate?) -> Unit = { _, _ -> }
) {
    var showPicker by remember { mutableStateOf(false) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var selectedDate by remember { mutableStateOf(initialDate ?: today) }
    var errorText by remember { mutableStateOf("") }

    fun validate(date: LocalDate?): Boolean {
        if (date == null) {
            errorText = "Please select a date"
            return false
        }
        if (minDate != null && date < minDate) {
            errorText = "Date before allowed range"
            return false
        }
        if (date > maxDate) {
            errorText = "Cannot select future dates"
            return false
        }
        errorText = ""
        return true
    }

    LaunchedEffect(selectedDate) {
        val valid = validate(selectedDate)
        onValidationChanged(valid, selectedDate)
        if (valid && selectedDate != null) onDateChanged(selectedDate!!)
    }

    Column(modifier = modifier.padding(vertical = 6.dp, horizontal = 8.dp)) {
        // Title
        if (title.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                fontSize = 14.sp
            )
        }

        // Readonly input-like box
        OutlinedTextFieldDefaults.DecorationBox(
            value = selectedDate?.toString() ?: "",
            innerTextField = {
                Text(
                    text = selectedDate?.toString() ?: hintText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedDate == null) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                )
            },
            enabled = true,
            singleLine = true,
            isError = errorText.isNotEmpty(),
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
            placeholder = {
                Text(
                    hintText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                )
            },
            label = null,
            contentPadding = compactInputPadding(),
            container = {
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = errorText.isNotEmpty(),
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    colors = ColorsUtils.getInputFieldColors(),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPicker = true }
                )
            },
            colors = ColorsUtils.getInputFieldColors()
        )

        // Error text
        if (errorText.isNotEmpty()) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }

    if (showPicker) {
        DateRangePickerDialog(
            onDismiss = { showPicker = false },
            onConfirm = { start, _ ->
                selectedDate = start
                showPicker = false
            },
            initialStart = selectedDate,
            initialEnd = selectedDate,
            minDate = minDate,
            maxDate = maxDate,
            selectionMode = SelectionMode.Single
        )
    }
}


