package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import edu.jc.corsage.masterani.Masterani.Masterani
import kotlinx.android.parcel.Parcelize

/**
 * Detailed Episode class
 */
@Parcelize
data class DetailedEpisode(val info: Info?, var thumbnail: String?) : Parcelable {
    val getThumbnail: String?
        get() {
            if (thumbnail != null) {
                return Masterani.CDN.EPISODE_THUMBNAIL_URL + thumbnail
            }
            return null
        }

    @Parcelize
    data class Info(var episode: String?,
                    val title: String?,
                    val aired: String?,
                    val duration: Int?,
                    val description: String?) : Parcelable {

        val getDurationText: String?
            get() {
                if (duration == null || duration == 0) {
                    return "23 MIN (Average)"
                } else {
                    return duration.toString() + " MIN"
                }
            }

        val getEpisodeText: String?
            get() {
                if (episode != null) {
                   return "EP. " + episode
                }
                return episode
            }
    }
}
