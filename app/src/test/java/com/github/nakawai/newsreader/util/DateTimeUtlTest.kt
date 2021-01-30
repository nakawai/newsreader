package com.github.nakawai.newsreader.util

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.ZoneOffset

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
    }
}
