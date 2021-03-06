package com.github.nakawai.newsreader.presentation.articles


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.util.CoroutinesTestRule
import com.github.nakawai.newsreader.util.observeForTesting
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


/**
 * [TopStoriesViewModel] Test
 */
class TopStoriesViewModelTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun start() {
        // Arrange
        val viewModel = TopStoriesViewModel(mockk(), mockk())

        // Act
        viewModel.start(Section.HOME)

        // Assert
        viewModel.topStoryUiModels.observeForTesting {
            assertThat(viewModel.topStoryUiModels.value).isEqualTo(null)
        }
    }
}
