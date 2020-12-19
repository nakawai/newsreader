package com.github.nakawai.newsreader.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.nakawai.newsreader.domain.model.NYTimesModel
import com.github.nakawai.newsreader.presentation.PresentationTranslator
import com.github.nakawai.newsreader.presentation.search.list.SearchResultUiModel
import kotlinx.coroutines.*

class SearchViewModel(
    private val model: com.github.nakawai.newsreader.domain.model.NYTimesModel
) : ViewModel() {

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _articles = MutableLiveData<List<SearchResultUiModel>>()
    val articles: LiveData<List<SearchResultUiModel>> = _articles

    private val _articles2 = MutableLiveData<List<SearchResultUiModel>>()
    val articles2: LiveData<List<SearchResultUiModel>> = _articles2

    private val _articles3 = MutableLiveData<List<SearchResultUiModel>>()
    val articles3: LiveData<List<SearchResultUiModel>> = _articles3

    private var job: Job? = null

    fun onQueryTextSubmit(query: String) {
        job?.cancelChildren()

        job = viewModelScope.launch(Dispatchers.Main) {
            _isLoading.value = true
            val async1 = async {
                runCatching {
                    model.searchArticle(query)
                        .map { PresentationTranslator.translate(it) }
                }
            }
            val async2 = async {
                runCatching {
                    model.searchArticle("election")
                        .map { PresentationTranslator.translate(it) }
                }
            }
            val async3 = async {
                runCatching {
                    model.searchArticle("home")
                        .map { PresentationTranslator.translate(it) }
                }
            }
            val result1 = async1.await()
            val result2 = async2.await()
            val result3 = async3.await()

            when {
                result1.isFailure -> result1.exceptionOrNull()?.let { _error.value = it }
                result2.isFailure -> result2.exceptionOrNull()?.let { _error.value = it }
                result3.isFailure -> result3.exceptionOrNull()?.let { _error.value = it }
            }

            _articles.value = result1.getOrDefault(emptyList())
            _articles2.value = result2.getOrDefault(emptyList())
            _articles3.value = result3.getOrDefault(emptyList())

            _isLoading.value = false
            job = null
        }
    }
}

