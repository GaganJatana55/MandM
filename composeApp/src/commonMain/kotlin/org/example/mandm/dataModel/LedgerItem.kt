package org.example.mandm.dataModel

sealed class LedgerItem {

    abstract val id: Long
    abstract val dateTime: Long

    data class Milk(
        val data: MilkTransactionWithLogs
    ) : LedgerItem() {
        override val id: Long get() = data.transaction.id
        override val dateTime: Long get() = data.transaction.dateTimeStamp
    }

    data class Money(
        val data: MoneyTransactionWithLogs
    ) : LedgerItem() {
        override val id: Long get() = data.transaction.id
        override val dateTime: Long get() = data.transaction.dateTimeStamp
    }
}