package com.github.nakawai.newsreader.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import com.github.nakawai.newsreader.domain.entities.Story
import com.github.nakawai.newsreader.domain.entities.StoryUrl
import kotlinx.coroutines.*

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel(
    private val model: NYTimesModel
) : ViewModel() {
    private var storyUrl = MutableLiveData<StoryUrl>()

    private var job: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = Transformations.switchMap(storyUrl) {
        model.observeArticle(storyUrl = it)
    }
    val story: LiveData<Story> = _story

    fun start(storyUrl: StoryUrl) {
        this.storyUrl.value = storyUrl
    }

    fun onResume() {
        // Mark story as read if screen is visible for 2 seconds
        job = GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            storyUrl.value?.let {
                model.updateStoryReadState(it, read = true)
            }

        }
    }

    fun onPause() {
        job?.cancel()
    }

}
