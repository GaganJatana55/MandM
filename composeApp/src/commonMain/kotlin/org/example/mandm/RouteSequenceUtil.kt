package org.example.mandm

object RouteSequenceUtil {

    private object Keys {
        const val ROUTE_SEQUENCE_COUNTER = "route_sequence_counter"
    }

    /**
     * Returns the current sequence value, then increments and persists the counter.
     * If no value is stored, starts from 1.
     */
    fun nextSequence(): Int {
        val current = AppPreferences.getInt(Keys.ROUTE_SEQUENCE_COUNTER, 0)
        val next = if (current <= 0) 1 else current + 1
        AppPreferences.saveInt(Keys.ROUTE_SEQUENCE_COUNTER, next)
        return next
    }

    /**
     * Peek the current value without incrementing (0 if unset).
     */
    fun current(): Int = AppPreferences.getInt(Keys.ROUTE_SEQUENCE_COUNTER, 0)

    /**
     * Reset the counter to the provided value (default 0).
     */
    fun reset(to: Int = 0) {
        AppPreferences.saveInt(Keys.ROUTE_SEQUENCE_COUNTER, to)
    }
}


