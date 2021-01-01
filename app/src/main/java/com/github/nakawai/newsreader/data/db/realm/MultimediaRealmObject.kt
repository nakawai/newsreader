package com.github.nakawai.newsreader.data.db.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MultimediaRealmObject : RealmObject() {
    @PrimaryKey
    var url: String? = null

    var format: String? = null

    var height = 0

    var width = 0

    var type: String? = null

    var subtype: String? = null

    var caption: String? = null

    var copyright: String? = null

}
