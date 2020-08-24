package com.github.nakawai.newsreader.ui.articles

import androidx.lifecycle.*
import com.github.nakawai.newsreader.model.Repository
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import kotlinx.coroutines.launch

/**
 * Presenter class for controlling the Main Activity
 */
class ArticlesViewModel(
    private val repository: Repository,
    private val section: Section
) : ViewModel() {
    private val _articles = MediatorLiveData<List<Article>>().apply {
        addSource(repository.observeArticlesBySection(section)) { articles ->
            value = articles
        }
    }
    val storiesData: LiveData<List<Article>> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadData(force: Boolean) {
        _isLoading.value = true

        viewModelScope.launch {
            val articles = repository.loadNewsFeed(section, force)
            _articles.value = articles
            _isLoading.value = false
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: Repository, private val section: Section) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ArticlesViewModel(repository, section) as T
        }

    }
}
