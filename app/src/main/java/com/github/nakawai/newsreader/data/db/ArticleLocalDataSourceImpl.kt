package com.github.nakawai.newsreader.data.db

import androidx.lifecycle.LiveData
import com.github.nakawai.newsreader.data.db.livedata.LiveRealmData
import com.github.nakawai.newsreader.data.db.livedata.LiveRealmListData
import com.github.nakawai.newsreader.data.db.realm.StoryRealmObject
import com.github.nakawai.newsreader.data.db.realm.translate
import com.github.nakawai.newsreader.domain.datasource.ArticleLocalDataSource
import com.github.nakawai.newsreader.domain.entities.Article
import com.github.nakawai.newsreader.domain.entities.ArticleUrl
import com.github.nakawai.newsreader.domain.entities.Section
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// TODO close Realm instance
class ArticleLocalDataSourceImpl(private val realm: Realm) : ArticleLocalDataSource {


    // Converts data into a usable format and save it to Realm
    override suspend fun saveTopStories(articles: List<Article>) {
        if (articles.isEmpty()) return

        suspendCancellableCoroutine<Unit> { continuation ->

            realm.executeTransactionAsync({ r: Realm ->
                for (article in articles) {
                    // Find existing article in Realm (if any)
                    // If it exists, we need to merge the local state with the remote, because the local state
                    // contains more info than is available on the server.
//                    val persistedStory =
//                        r.where(StoryRealmObject::class.java)
//                            .equalTo(StoryRealmObject.URL, article.url.value)
//                            .findFirst()
//
//                    if (persistedStory != null) {
//                        if (persistedStory.updatedDate != article.updatedDate) {
//                            // TODO Update content
//                        }
//                    } else {

                    r.copyToRealmOrUpdate(article.translate())
//                    }
                }
                continuation.resume(Unit)
            }) { throwable: Throwable ->
                Timber.e(throwable, "Could not save data")
                continuation.resumeWithException(throwable)
            }
        }

    }

    // Return the data in Realm. The query result will be automatically updated when the network requests
    // save data in Realm
    override suspend fun readTopStoriesBySection(section: Section): List<Article> = suspendCoroutine { continuation ->
        val realmResults = realm.where(StoryRealmObject::class.java)
            .equalTo(StoryRealmObject.SECTION, section.value)
            .sort(StoryRealmObject.PUBLISHED_DATE, Sort.DESCENDING)
            .findAllAsync()

        val listener = RealmChangeListener<RealmResults<StoryRealmObject>> {

            realmResults.removeAllChangeListeners()
            realmResults.map { it.translate() }.let { articles ->
                continuation.resume(articles)
            }
        }

        realmResults.addChangeListener(listener)


    }

    override fun observeArticlesBySection(section: Section): LiveData<List<Article>> {
        return object : LiveRealmListData<StoryRealmObject, Article>(realm) {
            override fun runQuery(realm: Realm): RealmResults<StoryRealmObject> {
                return realm.where(StoryRealmObject::class.java)
                    .sort(StoryRealmObject.PUBLISHED_DATE, Sort.DESCENDING)
                    .equalTo(StoryRealmObject.SECTION, section.value)
                    .findAllAsync()
            }

            override fun translate(realmObject: StoryRealmObject): Article {
                return realmObject.translate()
            }
        }
    }


    override fun observeArticle(articleUrl: ArticleUrl): LiveData<Article> {

        return object : LiveRealmData<StoryRealmObject, Article>(realm) {
            override fun runQuery(realm: Realm): RealmResults<StoryRealmObject> {

                return realm.where(StoryRealmObject::class.java)
                    .equalTo(StoryRealmObject.URL, articleUrl.value)
                    .findAllAsync()
            }

            override fun translate(realmObject: StoryRealmObject): Article {
                return realmObject.translate()
            }

        }

    }

    override fun updateArticleReadState(articleUrl: ArticleUrl, read: Boolean) {

        realm.executeTransactionAsync({ realm ->
            val persistedStory = realm.where(StoryRealmObject::class.java)
                .equalTo(StoryRealmObject.URL, articleUrl.value)
                .findFirst()

            if (persistedStory != null) {
                persistedStory.isRead = read
            } else {
                Timber.e("Trying to update a story that no longer exists: %1\$s", articleUrl)
            }

            //realm.close()
        }, { throwable ->
            Timber.e(throwable, "Failed to save data.")
        })
    }

    fun close() {
        realm.close()
    }

}
