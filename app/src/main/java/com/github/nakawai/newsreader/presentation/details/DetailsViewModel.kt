package com.github.nakawai.newsreader.presentation.details

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import kotlinx.coroutines.*

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel(
    private val repository: ArticleRepository
) : ViewModel() {
    private var _storyUrl = MutableLiveData<ArticleUrl>()

    private var job: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = Transformations.switchMap(_storyUrl) {
        repository.observeArticle(storyUrl = it)
    }
    val article: LiveData<Article> = _story

    fun start(storyUrl: ArticleUrl) {
        this._storyUrl.value = storyUrl
    }

    fun onResume() {
        // Mark story as read if screen is visible for 2 seconds
        job = viewModelScope.launch(Dispatchers.Main) {
            delay(2000)
            _storyUrl.value?.let {
                repository.updateStoryReadState(it, read = true)
            }

        }
    }

    fun onPause() {
        job?.cancelChildren()
    }

}
