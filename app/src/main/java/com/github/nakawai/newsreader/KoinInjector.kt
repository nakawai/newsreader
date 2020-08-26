package com.github.nakawai.newsreader

import com.github.nakawai.newsreader.data.Repository
import com.github.nakawai.newsreader.domain.NewsReaderAppService
import com.github.nakawai.newsreader.ui.articles.ArticlesViewModel
import com.github.nakawai.newsreader.ui.details.DetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Repository() }
    single { NewsReaderAppService(get()) }
    viewModel { ArticlesViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}
