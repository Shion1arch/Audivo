
package com.mardous.booming.extensions.utilities

import android.database.Cursor

val Cursor?.isNullOrEmpty: Boolean
    get() = this == null || this.count == 0

fun Cursor?.iterateIfValid(consumer: Cursor.() -> Unit) {
    if (this?.moveToFirst() == true) {
        do {
            consumer(this)
        } while (moveToNext())
    }
}

fun <T> Cursor?.mapIfValid(consumer: Cursor.() -> T): List<T> {
    val list = mutableListOf<T>()
    iterateIfValid {
        list.add(consumer(this))
    }
    return list
}

fun <T> Cursor?.takeOrDefault(default: T, consumer: Cursor.() -> T): T {
    return if (this != null && this.moveToFirst()) consumer(this) else default
}

fun Cursor.getBoolean(columnIndex: Int): Boolean {
    if (columnIndex != -1) {
        return getInt(columnIndex) == 1
    }
    return false
}

fun Cursor.getLongSafe(column: String): Long {
    val index = getColumnIndex(column)
    return if (index != -1) getLong(index) else -1
}

fun Cursor.getStringSafe(column: String): String? {
    val index = getColumnIndex(column)
    return if (index != -1) getString(index) else null
}