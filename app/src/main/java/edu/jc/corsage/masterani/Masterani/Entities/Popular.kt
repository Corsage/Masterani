package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by j3chowdh on 9/14/2017.
 */

@Parcelize
data class Popular(val being_watched: List<Anime>, val popular_today: List<Anime>) : Parcelable