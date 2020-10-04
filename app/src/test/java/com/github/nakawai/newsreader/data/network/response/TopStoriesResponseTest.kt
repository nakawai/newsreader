package com.github.nakawai.newsreader.data.network.response

import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import org.junit.After
import org.junit.Before
import org.junit.Test

class TopStoriesResponseTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testParse() {
        // Arrange
        val json = ClassLoader.getSystemResource("JSON/topstories_world.json").readText()

        // Actual
        val actual = Moshi.Builder().build().adapter(TopStoriesResponse::class.java).fromJson(json)!!

        // Assert
        assertThat(actual.status).isEqualTo("OK")
        assertThat(actual.lastUpdated).isEqualTo("2020-10-03T12:30:38-04:00")
        assertThat(actual.numResults).isEqualTo(37)
        assertThat(actual.results).hasSize(37)

        with(actual.results!![0]) {
            assertThat(section).isEqualTo("world")
            assertThat(url).isEqualTo("https://www.nytimes.com/2020/10/03/world/europe/germany-reunification-far-right.html")
            assertThat(createdDate).isEqualTo("2020-10-03T00:10:10-04:00")

            with(multimedia!![0]) {
                assertThat(url).isEqualTo("https://static01.nyt.com/images/2020/10/03/world/03reunification-farright4/merlin_172580730_47b5e877-068c-40fc-b5a5-d489ab11c7d8-superJumbo.jpg")
            }
        }

    }

}
