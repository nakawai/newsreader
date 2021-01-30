package com.github.nakawai.newsreader.util

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

class DateTimeUtlTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testParseOffsetDateTime() {
        // Arrange
        val dateText = "2020-10-03T13:30:38-04:00"

        // Act
        val actual = DateTimeUtl.parseToOffsetDateTime(dateText)!!

        // Assert
        assertThat(actual.year).isEqualTo(2020)
        assertThat(actual.monthValue).isEqualTo(10)
        assertThat(actual.dayOfMonth).isEqualTo(3)
        assertThat(actual.hour).isEqualTo(13)
        assertThat(actual.minute).isEqualTo(30)
        assertThat(actual.second).isEqualTo(38)
        assertThat(actual.offset).isEqualTo(ZoneOffset.ofHours(-4))

    }

    @Test
    fun testParseZonedDateTime() {
        // Arrange
        val dateText = "2020-10-03T13:30:38-04:00"

        // Act
        val actual = DateTimeUtl.parseToZonedDateTime(dateText)!!

        // Assert
        assertThat(actual.year).isEqualTo(2020)
        assertThat(actual.monthValue).isEqualTo(10)
        assertThat(actual.dayOfMonth).isEqualTo(3)
        assertThat(actual.hour).isEqualTo(13)
        assertThat(actual.minute).isEqualTo(30)
        assertThat(actual.second).isEqualTo(38)
        assertThat(actual.offset).isEqualTo(ZoneOffset.ofHours(-4))
        assertThat(actual.zone).isEqualTo(ZoneId.of("-04:00"))
    }

    @Test
    fun testYYYYMMDDParseZonedDateTime() {
        // Arrange
        val dateText = "2020-10-03"

        // Act
        val actual = DateTimeUtl.parseToLocalDateYyyyMmDd(dateText)!!

        // Assert
        assertThat(actual.year).isEqualTo(2020)
        assertThat(actual.monthValue).isEqualTo(10)
        assertThat(actual.dayOfMonth).isEqualTo(3)
    }

    @Test
    fun hoge() {

        // Act
        val tokyo = TimeZone.getTimeZone("Asia/Tokyo")
        val utc = TimeZone.getTimeZone("UTC")


        val c1 = Calendar.getInstance(tokyo).also {
            it.set(2020, 1, 1, 0, 0, 0)
        }

        val c2 = Calendar.getInstance(utc).also {
            it.set(2020, 1, 1, 0, 0, 0)
        }
        // Assert
        assertThat(c1.time.time).isEqualTo(c2.time.time)

    }

}
