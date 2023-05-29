package com.example.androidchallenge.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class DateTimeUtils {

    companion object {

        fun formatDate(dateTimeString: String): String {
            val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
            val myDate = LocalDateTime.parse(dateTimeString, inputFormatter)
            val dateTime = LocalDateTime.parse(myDate.toString())
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
            return dateTime.format(formatter)
        }
    }
}