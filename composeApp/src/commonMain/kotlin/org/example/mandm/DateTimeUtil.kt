package org.example.mandm


import kotlin.time.Duration.Companion.days
import kotlinx.datetime.*

object DateTimeUtil {

    private val utc = TimeZone.UTC
    private val local = TimeZone.currentSystemDefault()

    // ✅ 1. Current timestamp in millis (UTC)
    fun currentUtcMillis(): Long = Clock.System.now().toEpochMilliseconds()

    // ✅ 2. Current date as "YYYY-MM-DD" (UTC)
    fun currentUtcDateString(): String =
        Clock.System.now().toLocalDateTime(utc).date.toString()

    // ✅ 3. Format any timestamp to "1:22 PM"
    fun formatTo12HrTime(timestampMillis: Long): String {
        val time = Instant.fromEpochMilliseconds(timestampMillis)
            .toLocalDateTime(local).time

        val hour = time.hour % 12
        val displayHour = if (hour == 0) 12 else hour
        val minute = time.minute.toString().padStart(2, '0')
        val period = if (time.hour < 12) "AM" else "PM"

        return "$displayHour:$minute $period"
    }

    // ✅ 4. Extract "YYYY-MM-DD" from a timestamp (UTC)
    fun utcDateFromMillis(millis: Long): String {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(utc).date.toString()
    }

    // ✅ 5. Extract local time object (optional use)
    fun localTimeFromMillis(millis: Long): LocalTime {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(local).time
    }
}
