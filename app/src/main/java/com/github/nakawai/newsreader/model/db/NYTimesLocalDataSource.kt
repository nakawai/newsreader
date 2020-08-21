package com.github.nakawai.newsreader.model.db

import com.github.nakawai.newsreader.model.entity.Article
import com.github.nakawai.newsreader.model.entity.Multimedia
import com.github.nakawai.newsreader.model.entity.NYTimesStory
import io.realm.Realm
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

    private fun translate(story: NYTimesStory): Article {
        val mediaArray = mutableListOf<Multimedia>()
        story.multimedia?.forEach {
            mediaArray.add(Multimedia(it.url.orEmpty()))
        }
        return Article(
            title = story.title.orEmpty(),
            url = story.url.orEmpty(),
            storyAbstract = story.storyAbstract.orEmpty(),
            multimedia = mediaArray
        )
    }

    suspend fun loadStory(storyId: String): Article? = withContext(Dispatchers.IO) {
        val realm: Realm = Realm.getDefaultInstance()
        val story = realm.where(NYTimesStory::class.java)
            .equalTo(NYTimesStory.URL, storyId)
            .findFirst()

        return@withContext if (story != null && story.isValid && story.isLoaded) {
            translate(story)
        } else {
            null
        }

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