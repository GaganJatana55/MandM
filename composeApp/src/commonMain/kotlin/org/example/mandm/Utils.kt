package org.example.mandm

fun Double.formatMoney(): String {
    val rounded = (this * 100).toInt() / 100.0
    return if (rounded % 1.0 == 0.0) {
        "${rounded.toInt()} Rs"
    } else {
        buildString {
            append(rounded.toString())
            append(" Rs")
        }
    }
}
