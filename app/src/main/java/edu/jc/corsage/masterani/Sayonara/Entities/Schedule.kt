package edu.jc.corsage.masterani.Sayonara.Entities

import android.os.Parcelable
import edu.jc.corsage.masterani.Masterani.Entities.SortAnime
import kotlinx.android.parcel.Parcelize

/**
 * Created by j3chowdh on 10/21/2017.
 */

@Parcelize
data class Schedule(val day_of_week: Int, val release_time: String?, val next_episode: Int, val anime: ScheduleAnime) : Parcelable {
    val getReleaseTime: String
    get() {
        if (release_time == null || release_time == "null") {
            return "¯\\_(ツ)_/¯"
        } else {
            return release_time
        }
    }

    @Parcelize
    data class ScheduleAnime(val id: Int, val title: String, val poster: SortAnime.Poster) : Parcelable
}