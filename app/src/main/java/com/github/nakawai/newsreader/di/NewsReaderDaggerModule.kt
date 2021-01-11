package com.github.nakawai.newsreader.di

import com.github.nakawai.newsreader.data.db.ArticleLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.ConfigLocalDataSourceImpl
import com.github.nakawai.newsreader.data.network.ArticleRemoteDataSourceImpl
import com.github.nakawai.newsreader.domain.repository.ArticleRepository
import com.github.nakawai.newsreader.domain.repository.ArticleRepositoryImpl
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSource
import com.github.nakawai.newsreader.domain.repository.HistoryLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object NewsReaderDaggerModule {

    @Provides
    fun provideArticleRepository(): ArticleRepository {
        return ArticleRepositoryImpl(
            ArticleLocalDataSourceImpl(),
            ArticleRemoteDataSourceImpl(),
            ConfigLocalDataSourceImpl()
        )
    }

    @Provides
    fun provideHistoryRepository(): HistoryLocalDataSource {
        return HistoryLocalDataSourceImpl()
    }

}
