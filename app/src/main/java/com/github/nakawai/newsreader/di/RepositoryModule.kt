package com.github.nakawai.newsreader.di

import com.github.nakawai.newsreader.data.db.AppLocalDataSourceImpl
import com.github.nakawai.newsreader.data.db.NYTimesLocalDataSourceImpl
import com.github.nakawai.newsreader.data.network.NYTimesRemoteDataSourceImpl
import com.github.nakawai.newsreader.domain.model.ArticleRepository
import com.github.nakawai.newsreader.domain.model.ArticleRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object RepositoryModule {

    @Provides
    fun provideNYTimesRepository(): ArticleRepository {
        return ArticleRepositoryImpl(
            NYTimesLocalDataSourceImpl(),
            NYTimesRemoteDataSourceImpl(),
            AppLocalDataSourceImpl()
        )
    }
}
