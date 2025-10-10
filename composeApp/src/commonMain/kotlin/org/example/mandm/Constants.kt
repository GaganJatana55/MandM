package org.example.mandm

object TransactionTypeConstants{
    object Money{
        const val PAID="Paid"
        const val RECEIVED="Received"
    }
    object Milk{
    const val BUY="Buy"
    const val SELL="Sold"}
}

object TransactionStatus{
    const val PENDING="Pending"
    const val SKIPPED="Skipped"
    const val ADDED="Added"
}

// ---- User types ----
object UserTypeConstants{
    const val BUYER = "Buyer"
    const val SELLER = "Seller"
}

// ---- App roles ----
enum class RoleType(val displayName: String) {
    Dodhi("Dodhi"),
    HomeUser("Home User")
}

// ---- Bottom navigation reset policy ----
enum class TabResetPolicy {
    None,            // keep stacks
    ResetOnReselect, // tap current tab -> pop to root
    ResetOnEnter,    // switching to tab -> recreate at root
    Both             // both behaviors
}

object NavigationConfig {
    // Change policy here
    val currentTabResetPolicy: TabResetPolicy = TabResetPolicy.ResetOnReselect
}

// ---- Price mode for milk transaction ----
enum class PriceMode {
    FixPrice,
    SnfPrice
}