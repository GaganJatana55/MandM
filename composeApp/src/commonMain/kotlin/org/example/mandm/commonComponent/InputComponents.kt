package org.example.mandm.commonComponent

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.mandm.ColorsUtils
import org.example.mandm.compactInputPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneInputWithValidation(
    modifier: Modifier = Modifier,
    title: String = "Phone Number",
    initialValue: String = "",
    hintText: String = "Enter 10-digit number",
    disabled: Boolean = false,
    onValidationChanged: (Boolean, String) -> Unit = { _, _ -> }
) {
    var value by remember { mutableStateOf(initialValue) }
    var errorText by remember { mutableStateOf("") }
    var hasFocusedOnce by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    fun validatePhone(input: String): Boolean {
        return when {
            input.isBlank() -> {
                errorText = "" // don’t show error for empty
                false
            }

            input.length != 10 -> {
                errorText = "Must be 10 digits"
                false
            }

            !input.all { it.isDigit() } -> {
                errorText = "Only digits allowed"
                false
            }

            else -> {
                errorText = ""
                true
            }
        }
    }

    val isError = errorText.isNotEmpty() && (hasFocusedOnce || isFocused) && value.isNotEmpty()
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier.padding(vertical = 6.dp, horizontal = 8.dp)
    ) {
        // Title
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            fontSize = 14.sp
        )

        // Input using BasicTextField + DecorationBox
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                value = newValue
                val isValid = validatePhone(newValue)
                onValidationChanged(isValid, newValue)
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 18.sp
            ),
            enabled = !disabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) hasFocusedOnce = true
                },
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = !disabled,
                    singleLine = true,
                    isError = isError,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            hintText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        )
                    },
                    leadingIcon = {
                        Text(
                            "+91",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Light,
                                lineHeight = 16.sp,
                                fontSize = 14.sp
                            ),
                        )
                    },
                    label = null, // we already have a title above
                    contentPadding = compactInputPadding(),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = !disabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = ColorsUtils.getInputFieldColors(),
                            shape = MaterialTheme.shapes.small
                        )
                    },
                    colors = ColorsUtils.getInputFieldColors()
                )
            }
        )

        // Error
        if (isError) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}


enum class ValidationType {
    Name, Required, Optional
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidatedInputField(
    modifier: Modifier = Modifier,
    title: String = "",
    initialValue: String = "",
    hintText: String = "",
    disabled: Boolean = false,
    validationType: ValidationType = ValidationType.Optional,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValidationChanged: (Boolean, String) -> Unit = { _, _ -> }
) {
    var value by remember { mutableStateOf(initialValue) }
    var errorText by remember { mutableStateOf("") }
    var hasFocusedOnce by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    fun validate(input: String): Boolean {
        return when (validationType) {
            ValidationType.Name -> {
                if (input.isBlank()) {
                    errorText = "Name is required"
                    false
                } else if (input.length < 3) {
                    errorText = "Name must be at least 3 characters"
                    false
                } else {
                    errorText = ""
                    true
                }
            }

            ValidationType.Required -> {
                if (input.isBlank()) {
                    errorText = "This field is required"
                    false
                } else {
                    errorText = ""
                    true
                }
            }

            ValidationType.Optional -> {
                errorText = ""
                true
            }
        }
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

        // Input field with custom padding
        val interactionSource = remember { MutableInteractionSource() }

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                value = newValue
                val isValid = validate(newValue)
                onValidationChanged(isValid, newValue)
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 18.sp
            ),
            enabled = !disabled,
            singleLine = false, // ✅ multiline allowed
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                    if (it.isFocused) hasFocusedOnce = true
                },
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = !disabled,
                    singleLine = false,
                    isError = errorText.isNotEmpty() && (hasFocusedOnce || isFocused),
                    visualTransformation = VisualTransformation.None,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            hintText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp
                            )
                        )
                    },
                    label = null, // we already draw title above
                    contentPadding = compactInputPadding(),
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = !disabled,
                            isError = errorText.isNotEmpty() && (hasFocusedOnce || isFocused),
                            interactionSource = interactionSource,
                            colors = ColorsUtils.getInputFieldColors(),
                            shape = MaterialTheme.shapes.small
                        )
                    },
                    colors = ColorsUtils.getInputFieldColors()
                )
            }
        )

        // Error message
        if (errorText.isNotEmpty() && (hasFocusedOnce || isFocused)) {
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 2.dp)
            )
        }
    }
}
