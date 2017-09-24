package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Episode data class.
 * Note: The first constructor is based off of releases api, which is the most minimal.
 */

@Parcelize
data class Episode(val episode: String,
                   val created_at: String,
                   val anime: Anime? = null) : Parcelable