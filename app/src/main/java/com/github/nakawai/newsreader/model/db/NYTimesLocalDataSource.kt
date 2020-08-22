package com.github.nakawai.newsreader.model.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Multimedia
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// TODO close Realm instance
class NYTimesLocalDataSource {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-d'T'HH:mm:ssZZZZZ", Locale.US)
    private val outputDateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.US)

    // Converts data into a usable format and save it to Realm
    suspend fun processAndAddData(sectionKey: String, stories: List<NYTimesStory>) {
        if (stories.isEmpty()) return

        suspendCancellableCoroutine<Unit> { continuation ->

            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransactionAsync({ r: Realm ->
                for (story in stories) {
                    val parsedPublishedDate = inputDateFormat.parse(story.publishedDate, ParsePosition(0))
                    story.sortTimeStamp = parsedPublishedDate.time
                    story.publishedDate = outputDateFormat.format(parsedPublishedDate)

                    // Find existing story in Realm (if any)
                    // If it exists, we need to merge the local state with the remote, because the local state
                    // contains more info than is available on the server.
                    val persistedStory =
                        r.where(NYTimesStory::class.java)
                            .equalTo(NYTimesStory.URL, story.url).findFirst()
                    if (persistedStory != null) {
                        // Only local state is the `read` boolean.
                        story.isRead = persistedStory.isRead
                    }

                    // Only create or update the local story if needed
                    if (persistedStory == null || persistedStory.updatedDate != story.updatedDate) {
                        story.apiSection = sectionKey
                        r.copyToRealmOrUpdate(story)
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
            list.add(translate(story))
        }

        return@withContext list
    }

    fun observeArticles(sectionKey: String): LiveData<List<Article>> {
        val realm: Realm = Realm.getDefaultInstance()
        val realmResults = realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.API_SECTION, sectionKey)
            .sort(NYTimesStory.PUBLISHED_DATE, Sort.DESCENDING)
            .findAllAsync()


        val liveData = MutableLiveData<List<Article>>()

        val list = mutableListOf<Article>()
        val listener = RealmChangeListener<RealmResults<NYTimesStory>> { results ->
            results.forEach { realmStory ->
                list.add(translate(realmStory))
            }
            liveData.postValue(list)
        }

        // TODO remove listener
        realmResults.addChangeListener(listener)

        return liveData
    }

    private fun translate(story: NYTimesStory): Article {
        val mediaArray = mutableListOf<Multimedia>()
        story.multimedia?.forEach {
            mediaArray.add(Multimedia(it.url.orEmpty()))
        }
        return Article(
            title = story.title.orEmpty(),
            url = story.url.orEmpty(),
            storyAbstract = story.storyAbstract.orEmpty(),
            multimedia = mediaArray,
            publishedDate = story.publishedDate,
            isRead = story.isRead
        )
    }

    fun observeStory(storyId: String): LiveData<Article> {
        val realm: Realm = Realm.getDefaultInstance()
        val realmResults = realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.URL, storyId)
            .findAllAsync()

        val liveData = MutableLiveData<Article>()

        val listener = RealmChangeListener<RealmResults<NYTimesStory>> { results ->
            val result = results[0]
            val article = if (result != null && result.isValid && result.isLoaded) {
                liveData.postValue(translate(result))
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

    fun updateStoryReadState(storyId: String, read: Boolean) {

        val realm: Realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({ realm ->
            val persistedStory =
                realm.where(NYTimesStory::class.java)
                    .equalTo(NYTimesStory.URL, storyId).findFirst()
            if (persistedStory != null) {
                persistedStory.isRead = read
            } else {
                Timber.e("Trying to update a story that no longer exists: %1\$s", storyId)
            }
        }) { throwable -> Timber.e(throwable, "Failed to save data.") }
    }

}
