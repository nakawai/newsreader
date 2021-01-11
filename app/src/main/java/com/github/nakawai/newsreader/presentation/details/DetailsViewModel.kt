package com.github.nakawai.newsreader.presentation.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import com.github.nakawai.newsreader.presentation.articles.ArticleUiModel
import kotlinx.coroutines.*

/**
 * Presenter class for controlling the Main Activity
 */
class DetailsViewModel @ViewModelInject constructor(
    private val repository: ArticleRepository,
    private val historyLocalDataSource: HistoryLocalDataSource
) : ViewModel() {
    private var _storyUrl = MutableLiveData<ArticleUrl>()

    private var job: Job? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _article = Transformations.switchMap(_storyUrl) {
        repository.observeArticle(articleUrl = it)
    }

    private val _histories = historyLocalDataSource.observeHistories()

    val articleUiModel: LiveData<ArticleUiModel> = MediatorLiveData<ArticleUiModel>().also { uiModel ->
        uiModel.addSource(_article) { article ->
            uiModel.value = ArticleUiModel(article, System.currentTimeMillis(), _histories.value?.find { it.url == article.url } != null)
        }
        uiModel.addSource(_histories) { histories ->
            val article = _article.value ?: return@addSource
            uiModel.value = ArticleUiModel(article, System.currentTimeMillis(), histories.find { it.url == article.url } != null)
        }
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
                historyLocalDataSource.addHistory(it)
            }

        }
    }

    fun onPause() {
        job?.cancelChildren()
    }

}
