package com.github.nakawai.newsreader.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.nakawai.newsreader.domain.NewsReaderAppService
import com.github.nakawai.newsreader.domain.entity.Story
import com.github.nakawai.newsreader.domain.entity.StoryUrl
import kotlinx.coroutines.*

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel(
    private val appService: NewsReaderAppService,
    private val storyUrl: StoryUrl
) : ViewModel() {
    private var job: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = appService.observeArticle(storyUrl)
    val story: LiveData<Story> = _story

    fun onResume() {
        // Mark story as read if screen is visible for 2 seconds
        job = GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            appService.markAsRead(storyUrl)
        }
    }

    fun onPause() {
        job?.cancel()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val model: NewsReaderAppService, private val storyUrl: StoryUrl) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return DetailsViewModel(model, storyUrl) as T
        }

    }

}
