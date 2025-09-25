package org.example.mandm.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.mandm.RoleType
import org.example.mandm.commonComponent.RoleOption
import org.example.mandm.commonComponent.ButtonRoundCorner
import org.example.mandm.commonComponent.CommonSurfaceCard
import org.example.mandm.commonComponent.FormSpacer
import org.example.mandm.commonComponent.GetCommonScaffoldWithColumnCenter
import org.example.mandm.commonComponent.PhoneInputWithValidation
import org.example.mandm.commonComponent.ValidatedInputField
import org.example.mandm.commonComponent.ValidationType
import org.example.mandm.mainBackground
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun SignInUI(modifier: Modifier = Modifier) {
    GetCommonScaffoldWithColumnCenter(modifier.fillMaxSize().mainBackground(), topBar = {

        Text(
            "Sign In",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp
        )

    }) {
Spacer(Modifier.height(40.dp))
        CommonSurfaceCard() {
            Column(Modifier.padding(8.dp)) {
                Text(
                    "Replace with logo here",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,

                )
                Spacer(Modifier.height(30.dp))
                PhoneInputWithValidation()
                Spacer(Modifier.height(20.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ButtonRoundCorner(text = "Sign In") {
                        /**Todo on click **/
                    }
                }
                Spacer(Modifier.height(30.dp))
            }

        }
    }


}

@Preview
@Composable
fun SignUpUI(modifier: Modifier = Modifier, onComplete: () -> Unit = {}) {
    val scrollState = rememberScrollState()
    GetCommonScaffoldWithColumnCenter(modifier.fillMaxSize().mainBackground(), topBar = {

        Text(
            "Milk Man",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp
        )

    }) {

        Spacer(Modifier.height(40.dp))
        CommonSurfaceCard() {

            Column(Modifier.padding(8.dp).verticalScroll(scrollState)) {
                Text(
                    "Replace with logo here",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,

                    )
                Spacer(Modifier.height(30.dp))
                PhoneInputWithValidation()
                FormSpacer()
                ValidatedInputField(modifier= Modifier, title = "Name (required)", hintText = "Enter your name", validationType = ValidationType.Name)
                FormSpacer()
                ValidatedInputField(modifier= Modifier, title = "Village (required)", hintText = "Enter your Village", validationType = ValidationType.Required)
                FormSpacer()
                ValidatedInputField(modifier= Modifier, title = "Address (required)", hintText = "Enter your Address", validationType = ValidationType.Required)
                FormSpacer()

                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ButtonRoundCorner(text = "Proceed") {
                        onComplete()
                    }
                }
                Spacer(Modifier.height(30.dp))
            }

        }
    }


}

@Preview
@Composable
fun SelectRole(modifier: Modifier = Modifier, onProceedToSignUp: () -> Unit = {}) {
    var selectedRole by remember { mutableStateOf<RoleType?>(null) }
    GetCommonScaffoldWithColumnCenter(modifier.fillMaxSize().mainBackground(), topBar = {

        Text(
            "Select Your Role.",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            fontSize = 34.sp
        )

    }) {
        Spacer(Modifier.height(40.dp))
        CommonSurfaceCard() {
            Column(Modifier.padding(8.dp)) {
                Text(
                    "Please choose the role as per your requirements.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,

                    )
                Spacer(Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    RoleOption(
                        modifier = Modifier.weight(1f),
                        title = RoleType.Dodhi.displayName,
                        selected = selectedRole == RoleType.Dodhi,
                        onClick = { selectedRole = RoleType.Dodhi }
                    )
                    Spacer(Modifier.width(12.dp))
                    RoleOption(
                        modifier = Modifier.weight(1f),
                        title = RoleType.HomeUser.displayName,
                        selected = selectedRole == RoleType.HomeUser,
                        onClick = { selectedRole = RoleType.HomeUser }
                    )
                }

                Spacer(Modifier.height(20.dp))
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    ButtonRoundCorner(text = "Proceed") {
                        if (selectedRole != null) {
                            onProceedToSignUp()
                        }
                    }
                }
                Spacer(Modifier.height(30.dp))
            }

        }
    }


}
