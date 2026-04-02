package org.example.mandm.screens.screenNavigationParams

import kotlinx.serialization.Serializable
import org.example.mandm.dataModel.RouteEntity
import kotlin.reflect.KClass
// Authentication Group
@Serializable object SignIn
@Serializable object SelectRole
@Serializable data class SignUp(val onComplete: Boolean = true)

// Main App Group (Tabs)
@Serializable object Home
@Serializable object Billing
@Serializable object Clients
@Serializable object Routes



val topLevelRoutes = listOf<KClass<out Any>>(
    Home::class,
    Billing::class,
    Clients::class,
    Routes::class
)


@Serializable
data class RoutesEditDetail(val routeId: Int,val routeName: String)
@Serializable
data class BillingDetailsParams(
    val userId: Long,
    val userName: String
)


