package org.example.mandm.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.mandm.commonComponent.GetCommonScaffoldWithColumnCenter
import org.example.mandm.dataModel.RouteEntity


@Composable
fun EditRouteUserScreen(routeId:Int,routeName: String) {
    GetCommonScaffoldWithColumnCenter(modifier = Modifier, topBar = {
        RouteEditTopBar(routeName)
    }) {

    }
}

@Composable
fun RouteEditTopBar(name: String) {

    Row(modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp)) {
        Text(
            text = "Edit Route : ",
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge
        )
        Text(text = name, modifier = Modifier, style = MaterialTheme.typography.titleLarge)
    }


}