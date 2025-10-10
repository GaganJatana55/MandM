package org.example.mandm.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.mandm.backgroundCommonCard
import org.example.mandm.commonComponent.ButtonRoundCorner
import org.example.mandm.commonComponent.CommonSurfaceCard
import org.example.mandm.commonComponent.GetCommonScaffoldWithColumnCenter
import org.example.mandm.dataModel.CustomerEntity
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun UserListHome(modifier: Modifier = Modifier, users: List<CustomerEntity>) {
    var showAddCleintDialog by rememberSaveable { mutableStateOf(false) }
    GetCommonScaffoldWithColumnCenter(modifier = modifier.fillMaxHeight(), topBar = {
        Row(modifier = Modifier.padding(6.dp)) {
            Text(
                "Customers",
                Modifier.padding(top = 8.dp, start = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                ButtonRoundCorner(text = "Add Client") {
                    showAddCleintDialog = true
                }
            }
        }

    }) {

        CommonSurfaceCard(modifier = Modifier) {
            UsersList(users)
            if (showAddCleintDialog) CreateUserDialog(onDismiss = { showAddCleintDialog = false })
        }
    }
}

@Composable
fun UsersList(
    users: List<CustomerEntity>,
    modifier: Modifier = Modifier.fillMaxSize().padding(8.dp)
) {
    Box(
        modifier,
        contentAlignment = if (users.isEmpty()) Alignment.Center else Alignment.TopStart
    ) {
        if (users.isNotEmpty()) {
            LazyColumn() {
                items(users) { user ->
                    UserListItem(user)

                }
            }
        } else {
            Text(
                "No Customers Yet.\nlets add new.",
                Modifier.padding(top = 8.dp, start = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun UserListItem(
    user: CustomerEntity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxWidth()
            .backgroundCommonCard()

    ) {

        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {

                    Text(
                        text = user.userName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp

                    )



                    Text(
                        text = user.nickName
                            ?: "------", // Treat as Nick name substitute if a nickname field isn't present
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Text(
                    modifier= Modifier.padding(vertical = 10.dp),
                    text = user.village,
                    style = MaterialTheme.typography.bodyMedium
                )

            }
        }
        HorizontalDivider()
    }
}


