package com.github.nakawai.newsreader.model.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Section
import com.github.nakawai.newsreader.model.network.NYTimesStoryResponseItem
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// TODO close Realm instance
class NYTimesLocalDataSource {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZZZZZ", Locale.US)
    private val outputDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    // Converts data into a usable format and save it to Realm
    suspend fun saveData(sectionKey: String, responseItems: List<NYTimesStoryResponseItem>) {
        if (responseItems.isEmpty()) return

        suspendCancellableCoroutine<Unit> { continuation ->

            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransactionAsync({ r: Realm ->
                for (responseItem in responseItems) {


                    // Find existing responseItem in Realm (if any)
                    // If it exists, we need to merge the local state with the remote, because the local state
                    // contains more info than is available on the server.
                    val persistedStory =
                        r.where(NYTimesStory::class.java)
                            .equalTo(NYTimesStory.URL, responseItem.url)
                            .findFirst()

                    if (persistedStory != null) {
                        if (persistedStory.updatedDate != responseItem.updatedDate) {
                            // TODO Update content
                        }
                    } else {
                        val story = Translator.translate(responseItem)
                        story.apiSection = sectionKey
                        r.copyToRealm(story)
                    }
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
    suspend fun readData(sectionKey: String): List<Article> = withContext(Dispatchers.IO) {

        val realm: Realm = Realm.getDefaultInstance()
        val results = realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.API_SECTION, sectionKey)
            .sort(NYTimesStory.PUBLISHED_DATE, Sort.DESCENDING)
            .findAll()

        val list = mutableListOf<Article>()
        results.forEach { story ->
            list.add(story.translate())
        }

        return@withContext list
    }

    fun observeArticles(section: Section): LiveData<List<Article>> {
        return object : LiveRealmData<NYTimesStory, Article>() {
            override fun runQuery(realm: Realm): RealmResults<NYTimesStory> {
                return realm.where(NYTimesStory::class.java)
                    .sort(NYTimesStory.PUBLISHED_DATE, Sort.DESCENDING)
                    .equalTo(NYTimesStory.API_SECTION, section.key)
                    .findAllAsync()
            }

            override fun translate(original: NYTimesStory): Article {
                return original.translate()
            }
        }
    }


    fun observeStory(storyId: String): LiveData<Article> {
        val realm: Realm = Realm.getDefaultInstance()
        val realmResults = realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.URL, storyId)
            .findAllAsync()

        val liveData = MutableLiveData<Article>()

        val listener = RealmChangeListener<RealmResults<NYTimesStory>> { results ->
            val result = results[0]
            if (result != null && result.isValid && result.isLoaded) {
                liveData.postValue(result.translate())
            } else {
                throw IllegalStateException("invalid story Id")
            }
        }

        // TODO remove listener
        realmResults.addChangeListener(listener)

        return liveData
    }


    fun close() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.close()
    }

    fun updateStoryReadState(storyUrl: String, read: Boolean) {

        val instance: Realm = Realm.getDefaultInstance()
        instance.executeTransactionAsync({ realm ->
            val persistedStory = realm.where(NYTimesStory::class.java)
                .equalTo(NYTimesStory.URL, storyUrl)
                .findFirst()

            if (persistedStory != null) {
                persistedStory.isRead = read
            } else {
                Timber.e("Trying to update a story that no longer exists: %1\$s", storyUrl)
            }
        }, { throwable ->
            Timber.e(throwable, "Failed to save data.")
        })
    }

}
