package com.github.nakawai.newsreader.presentation.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.HistoryRepository
import com.github.nakawai.newsreader.presentation.articles.ArticleUiModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {
    private var _storyUrl = MutableLiveData<ArticleUrl>()

    private var job: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _article = Transformations.switchMap(_storyUrl) {
        repository.observeArticle(articleUrl = it)
    }.asFlow()

    private val _histories = historyRepository.observeHistories()

    val articleUiModel: Flow<ArticleUiModel> = combine(_article, _histories) { article, histories ->
        ArticleUiModel(
            article = article,
            nowTimeMillis = System.currentTimeMillis(),
            isRead = histories.find { it.url == article.url } != null
        )
    }


    fun start(storyUrl: ArticleUrl) {
        this._storyUrl.value = storyUrl
    }

    fun onResume() {
        job = viewModelScope.launch(Dispatchers.Main) {
            _storyUrl.value?.let {
                // Mark story as read if screen is visible for 2 seconds
                delay(2000)
                //repository.updateStoryReadState(it, read = true)
                historyRepository.addHistory(it)
            }

        }
    }

    fun onPause() {
        job?.cancelChildren()
    }

}
