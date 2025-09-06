package com.example.todo.utils

import android.content.Context
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle

fun dateFormatter(context: Context): DateTimeFormatter {
    val locale = context.resources.configuration.locales[0]
    return DateTimeFormatter.ofPattern("EEE, MMM dd", locale)
        .withDecimalStyle(DecimalStyle.of(locale))
}

fun timeFormatter(context: Context): DateTimeFormatter {
    val locale = context.resources.configuration.locales[0]
    return DateTimeFormatter.ofPattern("hh:mm a", locale)
        .withDecimalStyle(DecimalStyle.of(locale))
}
