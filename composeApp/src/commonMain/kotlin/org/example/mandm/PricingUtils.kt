package org.example.mandm

/**
 * Centralized pricing calculator for milk transactions.
 * Keep the logic here so we can tweak/configure it later without touching UI code.
 */
object PricingUtils {

    /**
     * Calculates total amount based on current pricing mode and inputs.
     *
     * @param mode Price mode (FixPrice or SnfPrice)
     * @param pricePerLiter Used only in FixPrice mode
     * @param quantity Quantity in liters
     * @param snf SNF value (used only in SnfPrice mode)
     * @param snfPrice Price per SNF unit (used only in SnfPrice mode)
     */
    fun calculateMilkTotal(
        mode: PriceMode,
        pricePerLiter: Double?,
        quantity: Double?,
        snf: Double?,
        snfPrice: Double?
    ): Double {
        val q = quantity ?: 0.0
        return when (mode) {
            PriceMode.FixPrice -> (pricePerLiter ?: 0.0) * q
            PriceMode.SnfPrice -> (snf ?: 0.0) * (snfPrice ?: 0.0) * q
        }
    }
}



