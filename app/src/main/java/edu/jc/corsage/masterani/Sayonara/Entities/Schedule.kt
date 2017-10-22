package edu.jc.corsage.masterani.Sayonara.Entities

import android.os.Parcelable
import edu.jc.corsage.masterani.Masterani.Entities.SortAnime
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

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
            // Convert to user's timezone...
            val sdf = SimpleDateFormat("hh:mm", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("Etc/GMT+1")
            val date = sdf.parse(release_time)

            // Make UI-friendly time.
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.time = date

            if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                if (cal.get(Calendar.HOUR) == 0) {
                    return "12" + ":" + cal.get(Calendar.MINUTE) + " AM"
                }
                return cal.get(Calendar.HOUR).toString() + ":" + cal.get(Calendar.MINUTE) + " AM"
            } else {
                if (cal.get(Calendar.HOUR) == 0) {
                    return "12" + ":" + cal.get(Calendar.MINUTE) + " PM"
                }
                return cal.get(Calendar.HOUR).toString() + ":" + cal.get(Calendar.MINUTE) + " PM"
            }
        }
    }

    @Parcelize
    data class ScheduleAnime(val id: Int, val title: String, val poster: SortAnime.Poster) : Parcelable
}