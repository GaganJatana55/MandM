package org.example.mandm.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.example.mandm.commonComponent.ButtonRoundCorner
import org.example.mandm.commonComponent.GetCommonScaffoldForTabs
import org.example.mandm.viewModels.RoutesViewModel
import org.koin.compose.viewmodel.koinViewModel

data class RouteScreen(val selectedRouteId:Int?=null) : Screen {
    @Composable
    override fun Content() {

    }
}

@Composable
fun RouteListScreen(){
    val routesViewModel: RoutesViewModel = koinViewModel()
    GetCommonScaffoldForTabs(modifier = Modifier, topBar = {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues()),


            ) {
            Text(text = "Routes", modifier = Modifier, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            ButtonRoundCorner(modifier = Modifier.padding(8.dp), text = "Add Route")

        }

    }){

    }
}