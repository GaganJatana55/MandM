package org.example.mandm


import YearMonth
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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
    fun getCurrentYearMonth(): YearMonth {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
       return YearMonth(today.year, today.monthNumber)

    }



    fun localDateTimeToMillis(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int = 0,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {

        val localDateTime = LocalDateTime(
            year = year,
            monthNumber = month,
            dayOfMonth = day,
            hour = hour,
            minute = minute,
            second = second,
            nanosecond = 0
        )

        return localDateTime
            .toInstant(timeZone)
            .toEpochMilliseconds()
    }
    fun Long.toLocalDateTime(): LocalDateTime =
        Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())



    fun Long.toLocalDate(
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): LocalDate {
        return Instant
            .fromEpochMilliseconds(this)
            .toLocalDateTime(timeZone)
            .date
    }


    fun LocalDate.toStartOfDayMillis(
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {
        return this
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()
    }
    fun toMillis(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int = 0,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {
        return LocalDateTime(
            year = year,
            monthNumber = month,
            dayOfMonth = day,
            hour = hour,
            minute = minute,
            second = second,
            nanosecond = 0
        ).toInstant(timeZone).toEpochMilliseconds()
    }

}
