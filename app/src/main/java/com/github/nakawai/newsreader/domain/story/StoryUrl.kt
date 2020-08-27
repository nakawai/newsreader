package com.github.nakawai.newsreader.domain.story

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Value Object
 */
@Parcelize
data class StoryUrl(val value: String) : Parcelable
