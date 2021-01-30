package com.github.nakawai.newsreader.util

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtl {
    fun parseToOffsetDateTime(dateText: String): OffsetDateTime? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        return OffsetDateTime.from(formatter.parse(dateText))
    }

    fun parseToZonedDateTime(dateText: String): ZonedDateTime? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        return ZonedDateTime.from(formatter.parse(dateText))
    }

    fun parseToLocalDateYyyyMmDd(dateText: String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return runCatching { LocalDate.from(formatter.parse(dateText)) }.getOrNull()
    }
}
