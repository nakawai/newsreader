package com.github.nakawai.newsreader.data.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.nakawai.newsreader.data.Translator
import com.github.nakawai.newsreader.data.network.response.StoryResponseItem
import com.github.nakawai.newsreader.data.toData
import com.github.nakawai.newsreader.data.translate
import com.github.nakawai.newsreader.domain.entity.Section
import com.github.nakawai.newsreader.domain.entity.Story
import com.github.nakawai.newsreader.domain.entity.StoryUrl
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// TODO close Realm instance
class NYTimesLocalDataSource {

    // Converts data into a usable format and save it to Realm
    suspend fun saveData(section: Section, responseItems: List<StoryResponseItem>) {
        if (responseItems.isEmpty()) return

        suspendCancellableCoroutine<Unit> { continuation ->

            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransactionAsync({ r: Realm ->
                for (responseItem in responseItems) {
                    // Find existing responseItem in Realm (if any)
                    // If it exists, we need to merge the local state with the remote, because the local state
                    // contains more info than is available on the server.
                    val persistedStory =
                        r.where(StoryRealmObject::class.java)
                            .equalTo(StoryRealmObject.URL, responseItem.url)
                            .findFirst()

                    if (persistedStory != null) {
                        if (persistedStory.updatedDate != responseItem.updatedDate) {
                            // TODO Update content
                        }
                    } else {
                        val story = Translator.translate(responseItem)
                        story.apiSection = section.toData().value
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
    suspend fun readData(section: Section): List<Story> = withContext(Dispatchers.IO) {

        val realm: Realm = Realm.getDefaultInstance()
        val results = realm.where(StoryRealmObject::class.java)
            .equalTo(StoryRealmObject.API_SECTION, section.toData().value)
            .sort(StoryRealmObject.PUBLISHED_DATE, Sort.DESCENDING)
            .findAll()

        val list = mutableListOf<Story>()
        results.forEach { story ->
            list.add(story.translate())
        }

        return@withContext list
    }

    fun observeArticles(section: Section): LiveData<List<Story>> {
        return object : LiveRealmData<StoryRealmObject, Story>() {
            override fun runQuery(realm: Realm): RealmResults<StoryRealmObject> {
                return realm.where(StoryRealmObject::class.java)
                    .sort(StoryRealmObject.PUBLISHED_DATE, Sort.DESCENDING)
                    .equalTo(StoryRealmObject.API_SECTION, section.toData().value)
                    .findAllAsync()
            }

            override fun translate(realmObject: StoryRealmObject): Story {
                return realmObject.translate()
            }
        }
    }


    fun observeStory(storyUrl: StoryUrl): LiveData<Story> {
        val realm: Realm = Realm.getDefaultInstance()
        val realmResults = realm.where(StoryRealmObject::class.java)
            .equalTo(StoryRealmObject.URL, storyUrl.value)
            .findAllAsync()

        val liveData = MutableLiveData<Story>()

        val listener = RealmChangeListener<RealmResults<StoryRealmObject>> { results ->
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

    fun updateStoryReadState(storyUrl: StoryUrl, read: Boolean) {

        val instance: Realm = Realm.getDefaultInstance()
        instance.executeTransactionAsync({ realm ->
            val persistedStory = realm.where(StoryRealmObject::class.java)
                .equalTo(StoryRealmObject.URL, storyUrl.value)
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
