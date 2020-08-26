package com.github.nakawai.newsreader.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Value Object
 */
@Parcelize
data class StoryUrl(val value: String) : Parcelable
