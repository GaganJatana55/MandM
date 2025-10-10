package org.example.mandm.commonComponent

import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import org.example.mandm.paddingCommon
import org.example.mandm.roundCorner
import org.example.mandm.roundCornerBottom
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.*
import org.example.mandm.mainBackground
import org.example.mandm.roundCorneButton
import org.example.mandm.roundCornerTop
import org.example.mandm.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import org.example.mandm.TransactionTypeConstants
import org.example.mandm.UserTypeConstants

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
    Scaffold(topBar = { TopBar { topBar() } }, modifier = Modifier.mainBackground()) {
        Surface(
            modifier = modifier.padding(top = it.calculateTopPadding()).paddingCommon()
                .mainBackground(),
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
    disabled: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    onValueChange: (String) -> Unit = {},
) {
    var textValue by rememberSaveable { mutableStateOf(value) }
    var errorValue by rememberSaveable { mutableStateOf(errorText) }
    var userInteracted by rememberSaveable { mutableStateOf(false) }
    Column(modifier = modifier) {
        BasicTextField(
            value = textValue,
            onValueChange = {
                if (disabled) {
                    return@BasicTextField
                }
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

@Composable
fun BuySellSwitch(
    modifier: Modifier = Modifier,
    value: String = TransactionTypeConstants.Milk.BUY,
    onChange: (String) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(46.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp)
    ) {
        val isBuy = value == TransactionTypeConstants.Milk.BUY
        val isSell = value == TransactionTypeConstants.Milk.SELL
        Text(
            text = "Buy",
            modifier = Modifier
                .clickable { onChange(TransactionTypeConstants.Milk.BUY) }

                .background(
                    if (isBuy) AppColors.Green.copy(alpha = 0.18f) else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                ) .padding(horizontal = 10.dp, vertical = 6.dp),
            color = if (isBuy) AppColors.Green else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isBuy) FontWeight.SemiBold else FontWeight.Normal,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Sold",
            modifier = Modifier
                .clickable { onChange(TransactionTypeConstants.Milk.SELL) }

                .background(
                    if (isSell) AppColors.Red.copy(alpha = 0.18f) else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            color = if (isSell) AppColors.Red else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSell) FontWeight.SemiBold else FontWeight.Normal,
        )
    }
}

@Composable
fun BuyerSellerSwitch(
    modifier: Modifier = Modifier,
    value: String = UserTypeConstants.BUYER,
    onChange: (String) -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(46.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            .padding(horizontal = 6.dp)
    ) {
        val isBuyer = value == UserTypeConstants.BUYER
        val isSeller = value == UserTypeConstants.SELLER
        Text(
            text = "Buyer",
            modifier = Modifier
                .clickable { onChange(UserTypeConstants.BUYER) }
                .background(
                    if (isBuyer) AppColors.Green.copy(alpha = 0.18f) else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            color = if (isBuyer) AppColors.Green else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isBuyer) FontWeight.SemiBold else FontWeight.Normal,
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Seller",
            modifier = Modifier
                .clickable { onChange(UserTypeConstants.SELLER) }
                .background(
                    if (isSeller) AppColors.Red.copy(alpha = 0.18f) else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            color = if (isSeller) AppColors.Red else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSeller) FontWeight.SemiBold else FontWeight.Normal,
        )
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
                    text = { DropDownItem(value = "Test") },
                    onClick = { onItemSelected(it) }
                )
            }

        }
    }
}

@Preview
@Composable
fun DropDownItem(modifier: Modifier = Modifier, value: String = "Test") {
    Box(
        modifier = modifier.padding(horizontal = 6.dp, vertical = 10.dp)
            .background(MaterialTheme.colorScheme.onSurface), Alignment.CenterStart
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Composable
fun CustomColoredCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {


    Checkbox(
        modifier = modifier,
        checked = checked,
        onCheckedChange = {
            onCheckedChange(it)
        },
        colors = CheckboxDefaults.colors(
            checkedColor = MaterialTheme.colorScheme.secondary,          // box fill when checked
            uncheckedColor = MaterialTheme.colorScheme.primary,          // outline when unchecked
            checkmarkColor = Color.White         // tick color
        )
    )

}

@Preview
@Composable
fun TextInputWithTitle(
    modifier: Modifier = Modifier,
    title: String = "Title",
    value: String = "Name",
    hintText: String = "0.0",
    disabled: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    errorText: String = ""
) {
    Column(modifier = modifier.padding(vertical = 6.dp, horizontal = 8.dp)) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp
        )
        CommonInputBox(
            modifier = Modifier.padding(top = 4.dp),
            hintText = hintText,
            keyboardType = keyboardType,
            errorText = errorText,
            value = value,
            disabled = disabled,

            )
    }
}

@Preview
@Composable
fun ButtonRoundCorner(
    modifier: Modifier = Modifier,
    text: String = "Button",
    icon: Painter? = null, // ✅ optional icon
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.secondary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    onClick: () -> Unit = {}
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        shape = roundCorner()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
fun ButtonRectangular(
    modifier: Modifier = Modifier,
    text: String = "Button",
    color: Color = MaterialTheme.colorScheme.secondary,
    icon: Painter? = null, // pass drawable/vector painter here
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                color = color,
                shape = roundCorneButton() // rounded corners
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // ✅ Start drawable
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // ✅ Button text
            Text(
                text = text,
                color = AppColors.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview
@Composable
fun CommonSurfaceCard(
    modifier: Modifier = Modifier,
    contentAlignMent: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {

    Surface(
        modifier.mainBackground(),
        shape = roundCorner(),
        shadowElevation = 2.dp
    ) {
        Box(Modifier, contentAlignment = contentAlignMent) {
            content()
        }
    }

}


@Composable
fun TopSurfaceCard(
    modifier: Modifier = Modifier,
    contentAlignMent: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .mainBackground(),
        shape = roundCorneButton(),
        shadowElevation = 2.dp
    ) {
        Box(Modifier.paddingCommon(), contentAlignment = contentAlignMent) {
            content()
        }
    }
}

@Composable
fun BottomSurfaceCard(
    modifier: Modifier = Modifier,
    contentAlignMent: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .mainBackground().paddingCommon(),
        shape = roundCornerBottom(),
        shadowElevation = 2.dp
    ) {
        Box(Modifier.paddingCommon(), contentAlignment = contentAlignMent) {
            content()
        }
    }
}


@Composable
fun GetCommonScaffoldWithColumnCenter(
    modifier: Modifier = Modifier,
    topBarContentAlignment: Alignment = Alignment.TopStart,
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit,

    ) {
    Scaffold(topBar = {
        TopBar {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues()).padding(bottom = 8.dp),
                contentAlignment = topBarContentAlignment
            ) { topBar() }
        }
    }, modifier = Modifier.mainBackground().fillMaxSize()) {
        Surface(
            modifier = modifier.fillMaxSize().padding(top = it.calculateTopPadding())
        ) {
            Column(Modifier.fillMaxSize().mainBackground().paddingCommon()) {
                content()
            }
        }
    }
}

@Composable
fun FormSpacer(){
    Spacer(Modifier.height(2.dp))
}
