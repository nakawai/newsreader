package com.github.nakawai.newsreader.domain.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Value Object
 */
@Parcelize
data class ArticleUrl(val value: String) : Parcelable
