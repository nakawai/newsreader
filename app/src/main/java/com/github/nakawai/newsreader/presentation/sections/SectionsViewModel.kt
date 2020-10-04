package com.github.nakawai.newsreader.presentation.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.data.Repository
import com.github.nakawai.newsreader.domain.entities.Section

class SectionsViewModel(private val repository: Repository) : ViewModel() {

    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    fun start() {
        _sections.value = repository.loadSections()
    }
}
