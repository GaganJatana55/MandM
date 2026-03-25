import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.Clock
import kotlinx.datetime.*
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.left_icon
import mandm.composeapp.generated.resources.right_icon
import org.example.mandm.DateTimeUtil
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun MonthSelector(
    initialYearMonth: YearMonth? = null,
    onMonthSelected: (YearMonth) -> Unit
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val currentYearMonth = YearMonth(today.year, today.monthNumber)

    var selectedMonth by remember {
        mutableStateOf(initialYearMonth ?: currentYearMonth)
    }

    val isCurrentMonth = selectedMonth == currentYearMonth

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        // ⬅ Previous
        Image(
            modifier = Modifier.clickable {
                selectedMonth = selectedMonth.minus(1)
                onMonthSelected(selectedMonth)
            }.size(24.dp),
            painter = painterResource(Res.drawable.left_icon),
            contentDescription = "Previous Month"
        )

        // Center Text
        val monthName = selectedMonth.monthName
            .lowercase()
            .replaceFirstChar { it.uppercase() }

        val displayText =
            if (selectedMonth.year == currentYearMonth.year)
                monthName
            else
                "$monthName ${selectedMonth.year}"

        Text(
            text = displayText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )

        // ➡ Next
        Image(
            modifier = Modifier.clickable(
                enabled = !isCurrentMonth
            ) {
                selectedMonth = selectedMonth.plus(1)
                onMonthSelected(selectedMonth)
            }.size(24.dp),
            painter = painterResource(Res.drawable.right_icon),
            contentDescription = "Next Month"
        )
    }
}


@OptIn(ExperimentalTime::class)
data class YearMonth(
    val year: Int,
    val month: Int // 1-12
) {

    init {
        require(month in 1..12) { "Month must be between 1 and 12" }
    }

    val monthName: String
        get() = Month(month).name.lowercase().replaceFirstChar { it.uppercase() }

    fun plus(months: Int): YearMonth {
        val totalMonths = year * 12 + (month - 1) + months
        val newYear = totalMonths / 12
        val newMonth = (totalMonths % 12) + 1
        return YearMonth(newYear, newMonth)
    }

    fun minus(months: Int): YearMonth = plus(-months)

    // 🔥 START OF MONTH (Epoch millis)

    fun startOfMonthMillis(
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {

        val localDateTime = LocalDateTime(
            year = year,
            monthNumber = month,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )

        return localDateTime
            .toInstant(timeZone)
            .toEpochMilliseconds()
    }

    // 🔥 END OF MONTH (Epoch millis)
    fun endOfMonthMillis(
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {

        // First day of NEXT month
        val nextMonth = this.plus(1)

        val firstDayNextMonth = LocalDateTime(
            year = nextMonth.year,
            monthNumber = nextMonth.month,
            dayOfMonth = 1,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )

        // Convert to instant
        val instantNextMonth = firstDayNextMonth.toInstant(timeZone)

        // Subtract 1 millisecond
        return instantNextMonth
            .minus(1, DateTimeUnit.MILLISECOND, timeZone)
            .toEpochMilliseconds()
    }

    companion object {

        fun now(): YearMonth {
            val now = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault())

            return YearMonth(now.year, now.monthNumber)
        }

        private fun isLeapYear(year: Int): Boolean {
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        }
    }
}
