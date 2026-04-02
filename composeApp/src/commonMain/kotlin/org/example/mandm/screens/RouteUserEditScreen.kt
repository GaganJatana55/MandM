package org.example.mandm.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.delete
import mandm.composeapp.generated.resources.delete_light
import mandm.composeapp.generated.resources.drag
import mandm.composeapp.generated.resources.plus
import mandm.composeapp.generated.resources.search_icon
import org.example.mandm.commonComponent.BottomSurfaceCard
import org.example.mandm.commonComponent.ButtonRoundCorner
import org.example.mandm.commonComponent.CommonSurfaceCard
import org.example.mandm.commonComponent.GetCommonScaffoldWithColumnCenter
import org.example.mandm.dataModel.CustomerRouteItem
import org.example.mandm.roundCorner
import org.example.mandm.viewModels.CustomerViewModel
import org.example.mandm.viewModels.RoutesViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


@Composable
fun EditRouteUserScreen(routeId: Int, routeName: String) {
    val routeViewmodel: RoutesViewModel = koinViewModel()
    GetCommonScaffoldWithColumnCenter(modifier = Modifier, topBar = {
        RouteEditTopBar(routeName)
    }, bottomBar = {

        ButtonRoundCorner(text = "Save") {
            routeViewmodel.saveChanges {

            }
        }
    }) {
        CommonSurfaceCard {
            RouteUserEditList(routeId)
        }

    }
}

@Composable
fun RouteUserEditList(routeId: Int) {
    val clientViewModel: CustomerViewModel = koinViewModel()
    val state = clientViewModel.uiState.collectAsState()
    val routeViewmodel: RoutesViewModel = koinViewModel()
    val customerRouteItems = routeViewmodel.customerRouteItems

    var showAddUserDialog by rememberSaveable { mutableStateOf(-1) }
    LaunchedEffect(routeId) {
        routeViewmodel.loadRoute(routeId)
        clientViewModel.observeAllCustomers()
    }

    if (showAddUserDialog != -1) {
        AddUserToRouteDialog(showAddUserDialog, allCustomers = state.value.customers, onDismiss = {
            showAddUserDialog = -1
        }, onConfirm = { list, index ->
            routeViewmodel.addCustomersAtPosition(list, index)

        })
    }

    // 1. Create the scroll state
    val scrollState = rememberLazyListState()

    // 2. Create the reorder state
    val reorderableState = rememberReorderableLazyListState(scrollState) { from, to ->
        // index - 1 because we have a header item at index 0
        routeViewmodel.onMove(from.index - 1, to.index - 1)
    }


    LazyColumn(state = scrollState, modifier = Modifier.padding(vertical = 8.dp)) {
        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(Res.drawable.plus), contentDescription = "Add New",
                    Modifier.size(34.dp).padding(4.dp).clickable {
                        showAddUserDialog = 0
                    })
            }
        }
        itemsIndexed(
            items = customerRouteItems,
            key = { _, item -> "${item.id}-${item.customerId}" }) { index, item ->
            ReorderableItem(reorderableState, key = "${item.id}-${item.customerId}") { isDragging ->
                // When dragging, we lift the item and keep it on the top layer
                val elevation = if (isDragging) 8.dp else 0.dp

                Box(
                    modifier = Modifier
                        .shadow(elevation)
                        .zIndex(if (isDragging) 1f else 0f)

                ) {
                    Column {
                        RouteUserItem(item, dragModifier = Modifier.draggableHandle(), {
                            routeViewmodel.deleteItem(it)
                        })
                        // 2. THE PLUS BUTTON (Centered on the bottom edge)
                        if (!isDragging) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 2.dp), // Adjust this to push the plus down
                                contentAlignment = Alignment.BottomCenter
                            ) {

                                Image(
                                    painter = painterResource(Res.drawable.plus),
                                    contentDescription = "Add below",
                                    modifier = Modifier.size(34.dp).padding(4.dp).clickable {
                                        showAddUserDialog = index+1
                                    }
                                )
                            }
                        }

                    }
                }
            }
        }

    }
}


@Composable
fun RouteUserItem(
    item: CustomerRouteItem,

    dragModifier: Modifier,

    onDeleteClick: (item: CustomerRouteItem) -> Unit
) {
    // We add extra vertical padding to the container to make room for the
    // floating "plus" icons so they don't overlap the borders.


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 2.dp)
    ) {
        // 1. THE MAIN CARD
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),
            shape = RoundedCornerShape(8.dp),
            // Apply border directly to the surface/shape
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary),
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp), // Internal spacing
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sequence & Name
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier

                            .size(38.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                        color = MaterialTheme.colorScheme.onSurface,

                        ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = (item.sequenceNumber + 1).toString(),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface

                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.customerName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Nangal nickname",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Actions
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Delete Button
                    Image(
                        painter = painterResource(Res.drawable.delete_light), // Assuming this is delete
                        contentDescription = "Delete",
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onDeleteClick(item) }
                    )

                    Spacer(Modifier.width(8.dp))

                    // Drag Handle

                    Box(modifier = dragModifier) {
                        Image(
                            painter = painterResource(Res.drawable.drag),
                            contentDescription = "Drag item",
                            modifier = Modifier.size(44.dp).padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }


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