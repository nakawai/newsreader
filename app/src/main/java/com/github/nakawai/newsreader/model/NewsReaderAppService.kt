package com.github.nakawai.newsreader.model

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section

/**
 * Model class for handling the business rules of the app.
 */
class NewsReaderAppService private constructor(private val repository: Repository) {
    companion object {

        // This could be replaced by Dependency Injection for easier testing
        var instance: NewsReaderAppService? = null
            get() {
                if (field == null) {
                    val repository =
                        Repository()
                    field =
                        NewsReaderAppService(repository)
                }
                return field
            }
            private set

    }

    fun observeArticle(storyId: String): LiveData<Article> {
        return repository.observeArticle(storyId)
    }

    fun observeArticlesBySection(section: Section): LiveData<List<Article>> {
        return repository.observeArticlesBySection(section)
    }

    /**
     * Returns the news feed for the currently selected category.
     */
    suspend fun loadNewsFeed(section: Section, force: Boolean): List<Article> {
        return repository.loadNewsFeed(section, force)
    }

    /**
     * Marks a story as being read.
     */
    fun markAsRead(storyId: String, read: Boolean) {
        repository.updateStoryReadState(storyId, read)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        repository.close()
    }

}
