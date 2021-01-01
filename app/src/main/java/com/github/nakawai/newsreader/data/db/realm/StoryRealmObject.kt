package com.github.nakawai.newsreader.data.db.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class StoryRealmObject : RealmObject() {
    @PrimaryKey
    var url: String? = null

    var apiSection: String? = null

    var section: String? = null

    var subsection: String? = null

    var title: String? = null

    var storyAbstract: String? = null

    var byline: String? = null

    var itemType: String? = null

    var updatedDate: String? = null

    var createdDate: String? = null

    var publishedDate: Date? = null

    var materialTypeFacet: String? = null

    var kicker: String? = null

    var multimedia: RealmList<MultimediaRealmObject>? = null

    var sortTimeStamp: Long = 0

    var isRead = false

    companion object {
        const val PUBLISHED_DATE = "publishedDate"
        const val URL = "url"
        const val API_SECTION = "apiSection"
    }
}
