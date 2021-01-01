package com.github.nakawai.newsreader.di

import com.github.nakawai.newsreader.data.db.ArticleLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.ConfigLocalDataSourceImpl
import com.github.nakawai.newsreader.data.network.ArticleRemoteDataSourceImpl
import com.github.nakawai.newsreader.domain.datasource.ArticleLocalDataSource
import com.github.nakawai.newsreader.domain.datasource.ArticleRemoteDataSource
import com.github.nakawai.newsreader.domain.datasource.ConfigLocalDataSource
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.ArticleRepositoryImpl
import com.github.nakawai.newsreader.presentation.articles.TopStoriesViewModel
import com.github.nakawai.newsreader.presentation.details.DetailsViewModel
import com.github.nakawai.newsreader.presentation.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ArticleRemoteDataSource> { ArticleRemoteDataSourceImpl() }
    single<ArticleLocalDataSource> { ArticleLocalDataSourceImpl() }
    single<ConfigLocalDataSource> { ConfigLocalDataSourceImpl() }
    single<ArticleRepository> {
        ArticleRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    //viewModel { SectionsViewModel(get()) }
    viewModel { TopStoriesViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}
