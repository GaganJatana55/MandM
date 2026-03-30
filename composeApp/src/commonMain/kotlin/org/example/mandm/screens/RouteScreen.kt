package org.example.mandm.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.moon_svg
import mandm.composeapp.generated.resources.morning_sun
import org.example.mandm.commonComponent.ButtonRoundCorner
import org.example.mandm.commonComponent.CommonSurfaceCard
import org.example.mandm.commonComponent.GetCommonScaffoldWithColumnCenter
import org.example.mandm.dataModel.RouteEntity
import org.example.mandm.repo.DefaultRouteFactory.isMorningRoute
import org.example.mandm.screens.screenNavigationParams.BillingDetailsParams
import org.example.mandm.screens.screenNavigationParams.RoutesEditDetail
import org.example.mandm.viewModels.RoutesViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RouteListScreen() {
    val routesViewModel: RoutesViewModel = koinViewModel()
    val routesList by routesViewModel.routesList.collectAsState()
    GetCommonScaffoldWithColumnCenter(modifier = Modifier, topBar = {

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 10.dp),
verticalAlignment = Alignment.CenterVertically

            ) {
            Text(
                text = "Routes",
                modifier = Modifier,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            ButtonRoundCorner(modifier = Modifier.padding(8.dp), text = "Add Route")

        }

    }) {
        CommonSurfaceCard(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = routesList) { item ->
                    RouteItem(routeEntity = item)
                }
            }
        }
    }
}

@Composable
fun RouteItem(modifier: Modifier= Modifier, routeEntity: RouteEntity) {
    val navController=LocalNavController.current
    Column(modifier = modifier.padding(horizontal = 4.dp).clickable(onClick = {

        navController.navigate(
            RoutesEditDetail(routeEntity.routeId,routeEntity.routeName)
        )
    })) {
        Row(modifier = Modifier.padding(vertical = 14.dp, horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(32.dp),
                painter = painterResource(if(isMorningRoute(routeEntity)) Res.drawable.morning_sun else Res.drawable.moon_svg),
                contentDescription = "Icon"
            )
            Text(
                text = routeEntity.routeName,
                modifier = Modifier.padding(start = 12.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )


        }
        HorizontalDivider(thickness = 1.dp)
    }
}