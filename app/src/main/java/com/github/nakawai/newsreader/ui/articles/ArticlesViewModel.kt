package com.github.nakawai.newsreader.ui.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.domain.NewsReaderAppService
import com.github.nakawai.newsreader.domain.entity.Section
import com.github.nakawai.newsreader.domain.entity.Story
import kotlinx.coroutines.launch

/**
 * Presenter class for controlling the Main Activity
 */
class ArticlesViewModel(
    private val appService: NewsReaderAppService,
    private val section: Section
) : ViewModel() {
    private val _articles = MediatorLiveData<List<Story>>().apply {
        addSource(appService.observeArticlesBySection(section)) { articles ->
            value = articles
        }
    }
    val storiesData: LiveData<List<Story>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadData(force: Boolean) {
        _isLoading.value = true

        viewModelScope.launch {
            _articles.value = appService.loadNewsFeed(section, force)
            _isLoading.value = false
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val appService: NewsReaderAppService, private val section: Section) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(appService, section) as T
        }

    }
}
