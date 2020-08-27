package com.github.nakawai.newsreader.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.domain.StoryAppService
import com.github.nakawai.newsreader.domain.story.Story
import com.github.nakawai.newsreader.domain.story.StoryUrl
import kotlinx.coroutines.*

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel(
    private val appService: StoryAppService
) : ViewModel() {
    private lateinit var storyUrl: StoryUrl
    private var job: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = MediatorLiveData<Story>()
    val story: LiveData<Story> = _story

    fun start(storyUrl: StoryUrl) {
        this.storyUrl = storyUrl
        _story.addSource(appService.observeArticle(storyUrl)) {
            _story.value = it
            _isLoading.value = false
        }
    }

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

}
