package com.github.nakawai.newsreader.data.network.response.topstories


import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 *  [Reference](http://www.ne.jp/asahi/hishidama/home/tech/java/DateTimeFormatter.html)
 */
class StoryResponseItemTranslatorExtensionKtTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testParseDateTime() {
        // Arrange
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ")
        val dateText = "2020-10-03T13:30:38-04:00"

        // Act
        val actual = OffsetDateTime.from(formatter.parse(dateText))


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
