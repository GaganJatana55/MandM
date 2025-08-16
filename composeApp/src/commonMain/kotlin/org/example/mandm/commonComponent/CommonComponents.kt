package org.example.mandm.commonComponent

import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.left_icon
import org.example.mandm.backgroundCommonCard
import org.example.mandm.paddingCommon
import org.example.mandm.roundCorner
import org.example.mandm.roundCornerBottom
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.exp

@Composable
fun GetDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.height(1.dp).background(MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun GetCommonScaffoldForTabs(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(topBar = { TopBar { topBar() } }, modifier = Modifier.background(Color.Transparent)) {
        Surface(
            modifier = modifier.padding(top = it.calculateTopPadding()).paddingCommon()
                .backgroundCommonCard(),
            shape = roundCorner(),
            shadowElevation = 2.dp
        ) {
            Column {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBar(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    return Surface(
        modifier = modifier.padding(horizontal = 8.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface, // or MaterialTheme.colorScheme.surface
        shape = roundCornerBottom(),
        shadowElevation = 4.dp
    ) {
        content()

    }

}

@Preview
@Composable
fun RoundedSideIconButton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(topStart = 40.dp, bottomStart = 40.dp),
    iconPainter: Painter = painterResource(Res.drawable.left_icon),
    contentDescription: String = "Button",
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.height(70.dp).width(35.dp)
            .clickable(onClick = onClick),
        shape = shape,
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp), // controls icon padding
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = iconPainter,
                contentDescription = contentDescription
            )
        }
    }
}

@Preview
@Composable
fun CommonInputBox(
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 10.dp),
    value: String = "",
    hintText: String = "Search",
    errorText: String = "",
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    onValueChange: (String) -> Unit = {},
) {
    var textValue by rememberSaveable { mutableStateOf(value) }
    var errorValue by rememberSaveable { mutableStateOf(value) }
    var userInteracted by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        BasicTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                errorValue = ""
                onValueChange.invoke(it)
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            textStyle = MaterialTheme.typography.labelSmall,
            singleLine = true,
            modifier = Modifier
                .heightIn(42.dp).background(MaterialTheme.colorScheme.surface)
                .border(
                    1.5.dp,
                    if (errorValue.isEmpty()) {
                        Color.LightGray
                    } else {
                        Color.Red
                    },
                    RoundedCornerShape(12.dp)
                ).onFocusEvent({
                    userInteracted = userInteracted || it.isFocused
                })
                .padding(6.dp),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 4.dp) // inner padding
                ) {

                    Box(Modifier.weight(1f).padding(horizontal = 6.dp)) {
                        if (textValue.isEmpty()) {
                            Text(
                                hintText,
                                color = Color.Gray,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )
        if (errorValue.isNotEmpty() && userInteracted) {
            Text(text = errorText, color = Color.Red, fontSize = 11.sp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AutoCompleteDropdown(
    modifier: Modifier = Modifier,
    list: List<T>,
    onItemSelected: (T) -> Unit,
) {
    var value by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {}, modifier = modifier) {

        CommonInputBox(
            value = value,
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryEditable,
                    enabled = true
                ).onFocusEvent({
                    expanded = it.isFocused || it.hasFocus
                }),
            onValueChange = {
                value = it
            }
        )

        ExposedDropdownMenu(
            expanded = expanded && list.isNotEmpty(),
            onDismissRequest = { expanded = false }) {
            list.forEach {
                DropdownMenuItem(
                    text = { DropDownItem(value="Test")},
                    onClick = { onItemSelected(it) }
                )
            }

        }
    }
}
@Preview
@Composable
fun DropDownItem(modifier: Modifier= Modifier,value: String="Test"){
    Box(modifier = modifier.padding(horizontal = 6.dp, vertical = 10.dp).background(MaterialTheme.colorScheme.onSurface), Alignment.CenterStart){
        Text(text = value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}
