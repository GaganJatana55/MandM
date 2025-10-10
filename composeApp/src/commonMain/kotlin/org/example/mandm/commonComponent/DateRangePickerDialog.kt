package org.example.mandm.commonComponent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import mandm.composeapp.generated.resources.Res
import mandm.composeapp.generated.resources.left_icon
import mandm.composeapp.generated.resources.right_icon
import org.example.mandm.DateTimeUtil
import org.example.mandm.backgroundCommonCard
import org.example.mandm.roundCorner
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.shape.CircleShape
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

enum class SelectionMode { Single, Range }

@Composable
fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (start: LocalDate, end: LocalDate) -> Unit,
    initialStart: LocalDate? = null,
    initialEnd: LocalDate? = null,
    minDate: LocalDate? = null,
    // By default, do not allow selecting dates after today
    maxDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    selectionMode: SelectionMode = SelectionMode.Range
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        var startDate by remember { mutableStateOf(initialStart) }
        var endDate by remember { mutableStateOf(initialEnd) }

        // Current visible month/year in calendar
        var currentMonthYear by remember {
            val base = (initialStart ?: maxDate)
            mutableStateOf(base.year to base.month)
        }

        Surface(shape = roundCorner()) {
            Column(Modifier.padding(12.dp).background(MaterialTheme.colorScheme.surface)) {
                Header(
                    year = currentMonthYear.first,
                    month = currentMonthYear.second,
                    onPrev = {
                        val y = currentMonthYear.first
                        val m = currentMonthYear.second
                        val prev = previousMonth(y, m)
                        // Do not go before minDate's month (if provided)
                        if (minDate == null || !isBeforeMonth(prev, minDate)) {
                            currentMonthYear = prev
                        }
                    },
                    onNext = {
                        val y = currentMonthYear.first
                        val m = currentMonthYear.second
                        val next = nextMonth(y, m)
                        // Do not go beyond maxDate's month
                        if (!isAfterMonth(next.first, next.second, maxDate)) {
                            currentMonthYear = next
                        }
                    }
                )

                HorizontalDivider()
                Spacer(Modifier.height(6.dp))

                WeekdayRow()

                Spacer(Modifier.height(6.dp))

                CalendarGrid(
                    year = currentMonthYear.first,
                    month = currentMonthYear.second,
                    startDate = startDate,
                    endDate = endDate,
                    minDate = minDate,
                    maxDate = maxDate,
                    onDayClick = { day ->
                        if (selectionMode == SelectionMode.Single) {
                            onConfirm(day, day)
                            onDismiss()
                        } else {
                            if (startDate == null) {
                                startDate = day
                                endDate = null
                            } else if (endDate == null) {
                                if (day < startDate!!) {
                                    startDate = day
                                    endDate = null
                                } else {
                                    endDate = day
                                }
                            } else {
                                startDate = day
                                endDate = null
                            }
                        }
                    }
                )

                if (selectionMode == SelectionMode.Range) {
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlineActionButton(
                            modifier = Modifier.weight(1f).heightIn(48.dp),
                            text = "Close",
                            borderColor = MaterialTheme.colorScheme.error
                        ) { onDismiss() }

                        val canApply = startDate != null
                        FilledActionButton(
                            modifier = Modifier.weight(1f).heightIn(48.dp),
                            text = "Apply",
                            enabled = canApply
                        ) {
                            val start = startDate!!
                            val end = endDate ?: start
                            onConfirm(start, end)
                            onDismiss()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    year: Int,
    month: Month,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(Res.drawable.left_icon),
            contentDescription = "Previous Month",
            modifier = Modifier.size(28.dp).clickable { onPrev() },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
        Text(
            text = monthName(month) + " " + year.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Image(
            painter = painterResource(Res.drawable.right_icon),
            contentDescription = "Next Month",
            modifier = Modifier.size(28.dp).clickable { onNext() },
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
        )
    }
}

@Composable
private fun WeekdayRow() {
    val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Row(Modifier.fillMaxWidth()) {
        labels.forEach { d ->
            Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(text = d, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    year: Int,
    month: Month,
    startDate: LocalDate?,
    endDate: LocalDate?,
    minDate: LocalDate?,
    maxDate: LocalDate,
    onDayClick: (LocalDate) -> Unit
) {
    val firstOfMonth = LocalDate(year, month, 1)
    val firstDow = dayOfWeekIndex(firstOfMonth.dayOfWeek) // 0..6 (Mon..Sun)
    val daysInMonth = daysInMonth(year, month)

    // Build 6 rows of 7 days
    val totalCells = 42
    val cells = (0 until totalCells).map { index ->
        val dayNum = index - firstDow + 1
        if (dayNum in 1..daysInMonth) LocalDate(year, month, dayNum) else null
    }

    Column(Modifier.fillMaxWidth()) {
        cells.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    Spacer(Modifier.weight(0.4f))
                    Box(Modifier.weight(1f).padding()) {
                        DayCell(
                            date = date,
                            startDate = startDate,
                            endDate = endDate,
                            minDate = minDate,
                            maxDate = maxDate,
                            onClick = onDayClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate?,
    startDate: LocalDate?,
    endDate: LocalDate?,
    minDate: LocalDate?,
    maxDate: LocalDate,
    onClick: (LocalDate) -> Unit
) {
    val empty = date == null
    val enabled = if (empty) false else isWithinBounds(date!!, minDate, maxDate)
    val selected = if (date != null) isSelected(date, startDate, endDate) else false
    val isEndpoint = date != null && (date == startDate || date == endDate)
    val inRange = selected && !isEndpoint
    val bg = if (inRange) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent
    Box(
        modifier = Modifier
            .height(36.dp)
            .padding(4.dp)
            .background(bg, shape = MaterialTheme.shapes.medium)
            .clickable(enabled = enabled && !empty) { onClick(date!!) },
        contentAlignment = Alignment.Center
    ) {
        if (!empty) {
            val dayNum = date!!.dayOfMonth
            val dayText = if (dayNum < 10) " $dayNum" else dayNum.toString()
            if (isEndpoint) {
                Box(
                    modifier = Modifier.size(28.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = dayText, color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.bodySmall)
                }
            } else {
                val textColor = when {
                    !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    inRange -> MaterialTheme.colorScheme.onSurface
                    else -> MaterialTheme.colorScheme.onSurface
                }
                Text(text = dayText, color = textColor, style = MaterialTheme.typography.bodySmall)
            }
        } else {
            Spacer(Modifier.width(0.dp))
        }
    }
}

// --- Helpers ---

private fun previousMonth(year: Int, month: Month): Pair<Int, Month> {
    return if (month == Month.JANUARY) (year - 1) to Month.DECEMBER else year to Month.entries[month.ordinal - 1]
}

private fun nextMonth(year: Int, month: Month): Pair<Int, Month> {
    return if (month == Month.DECEMBER) (year + 1) to Month.JANUARY else year to Month.entries[month.ordinal + 1]
}

private fun isBeforeMonth(target: Pair<Int, Month>, minDate: LocalDate): Boolean {
    val (y, m) = target
    return (y < minDate.year) || (y == minDate.year && m.ordinal < minDate.month.ordinal)
}

private fun isAfterMonth(targetYear: Int, targetMonth: Month, maxDate: LocalDate): Boolean {
    return (targetYear > maxDate.year) || (targetYear == maxDate.year && targetMonth.ordinal > maxDate.month.ordinal)
}

private fun monthName(month: Month): String = month.name.lowercase().replaceFirstChar { it.uppercase() }

private fun dayOfWeekIndex(dow: DayOfWeek): Int { // Monday=0
    // DayOfWeek.MONDAY.value == 1 ... SUNDAY == 7
    return dow.ordinal // Kotlinx DayOfWeek ordinal is 0..6 starting MONDAY
}

private fun daysInMonth(year: Int, month: Month): Int {
    val first = LocalDate(year, month, 1)
    val last = first.plus(DatePeriod(months = 1)).minus(DatePeriod(days = 1))
    return last.dayOfMonth
}

private fun isWithinBounds(date: LocalDate, minDate: LocalDate?, maxDate: LocalDate): Boolean {
    if (minDate != null && date < minDate) return false
    if (date > maxDate) return false
    return true
}

private fun isSelected(date: LocalDate, start: LocalDate?, end: LocalDate?): Boolean {
    if (start == null) return false
    val e = end ?: start
    return date >= start && date <= e
}


