package com.github.nakawai.newsreader.presentation.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SectionsViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val repository: ArticleRepository
) : ViewModel() {

    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    fun start() {
        _sections.value = repository.loadSections()
    }
}
