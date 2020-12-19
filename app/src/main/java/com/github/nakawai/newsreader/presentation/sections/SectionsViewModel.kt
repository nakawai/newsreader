package com.github.nakawai.newsreader.presentation.sections

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.nakawai.newsreader.domain.entities.Section
import com.github.nakawai.newsreader.domain.model.NYTimesRepository

class SectionsViewModel(private val model: NYTimesRepository) : ViewModel() {

    private val _sections = MutableLiveData<List<Section>>()
    val sections: LiveData<List<Section>> = _sections

    fun start() {
        _sections.value = model.loadSections()
    }
}
