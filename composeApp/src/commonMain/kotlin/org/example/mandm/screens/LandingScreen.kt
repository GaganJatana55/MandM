import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.home_icon
import mandm.composeapp.generated.resources.ledger_icon
import mandm.composeapp.generated.resources.settings_icon
import org.example.mandm.roundCornerBottom
import org.example.mandm.roundCornerTop
import org.jetbrains.compose.resources.DrawableResource

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview






@Composable
fun ThreeWaySwitch(selected: Int = 1, onSelect: (Int) -> Unit) {
    var selected by remember { mutableStateOf(selected) }
    Surface(
        modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 6.dp),
        shape = RoundedCornerShape(24.dp), // rounded background shape
        color = MaterialTheme.colorScheme.background, // light green background for container
        shadowElevation = 1.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)

        ) {
            listOf("Buy", "All", "Sell").forEachIndexed { index, label ->
                val isSelected = selected == index
                TextButton(
                    onClick = {
                        selected = index
                        onSelect(index)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.background,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text=label, fontWeight = FontWeight.SemiBold )
                }
            }
        }
    }
}


sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: DrawableResource
) {
    object Home : BottomNavItem("home", "Home",Res.drawable.home_icon )
    object Summary : BottomNavItem("summary", "Summary", Res.drawable.ledger_icon)
    object Users : BottomNavItem("users", "Users", Res.drawable.ledger_icon)
    object Settings : BottomNavItem("settings", "Settings", Res.drawable.settings_icon)
}
val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Summary,
    BottomNavItem.Users,
    BottomNavItem.Settings
)
@Preview
@Composable
fun BottomNavBar(
    selectedRoute: BottomNavItem = BottomNavItem.Home, // default selection
    items: List<BottomNavItem> = bottomNavItems, // default navigation items
    onItemSelected: (BottomNavItem) -> Unit = {}
) {
    var route by remember { mutableStateOf(selectedRoute) }
    Surface(
        modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
        shape = roundCornerTop(),
        color = MaterialTheme .colorScheme.surface,
        shadowElevation = 6.dp
    ) {

        NavigationBar(
            containerColor = MaterialTheme .colorScheme.surface
        ) {
            items.forEach { item ->
                val isSelected = route == item

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
route=item
                        onItemSelected(item) },
                    icon = {
                        Image(
                            painter = painterResource(item.icon),
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(
                                if (isSelected)
                                    MaterialTheme.colorScheme.secondary
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },
                    label = { Text(item.label) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.secondary,
                        selectedTextColor = MaterialTheme.colorScheme.secondary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = Color.Transparent
                    )

                )
            }
        }
    }
}

