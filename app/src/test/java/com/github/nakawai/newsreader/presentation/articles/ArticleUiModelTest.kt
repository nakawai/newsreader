package com.github.nakawai.newsreader.presentation.articles

import android.app.Application
import android.os.Build
import com.github.nakawai.newsreader.domain.entities.Multimedia
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.OffsetDateTime
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P], application = MockApp::class, manifest = Config.NONE)
class ArticleUiModelTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test() {
        // Arrange
        val story = Story(
            title = "title",
            storyAbstract = "storyAbstract",
            url = StoryUrl("url"),
            multimedia = listOf(Multimedia("url")),
            publishedDate = Date("2020-01-01T00:00:00+09:00".toEpochMilli()),
            isRead = false,
            section = Section.HOME
        )

        // Act
        val uiModel = ArticleUiModel(story, nowTimeMillis = "2020-01-01T00:01:00+09:00".toEpochMilli())

        // Assert
        assertThat(uiModel.relativeTimeSpanText).isEqualTo("1 minute ago")
    }
}

class MockApp : Application()

fun String.toEpochMilli(): Long {
    return OffsetDateTime.parse(this).toInstant().toEpochMilli()
}