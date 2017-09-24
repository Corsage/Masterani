package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import edu.jc.corsage.masterani.Masterani.Collection.Status
import edu.jc.corsage.masterani.Masterani.Collection.Type
import edu.jc.corsage.masterani.Masterani.Masterani
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Created by j3chowdh on 9/11/2017.
 */

@Parcelize
class DetailedAnime(val info: Info,
                    val poster: String? = null,
                    val genres: List<Genre>?,
                    val wallpapers: List<Wallpaper>?,
                    val episodes: List<DetailedEpisode>) : Parcelable {

    val getPoster: String?
        get() {
            if (poster != null) {
                return Masterani.CDN.DETAILED_POSTER_URL + poster
            } else {
                return null
            }
        }

    val getRandomWallpaper: String?
        get() {
            if (wallpapers != null) {
                return Masterani.CDN.WALLPAPER_URL + wallpapers[Random().nextInt(wallpapers.size)].file
            } else {
                return null
            }
        }

    @Parcelize
    class Search(val id: Int,
                 val title: String,
                 val started_airing_date: String?,
                 val type: Int) : Parcelable {

        val getInfoText: String?
            get() {
                // TYPE YEAR
                return Type(type).toString() + " " + started_airing_date
            }
    }

    @Parcelize
    class Info(val id: Int,
               val title: String,
               val slug: String,
               val synopsis: String,
               val status: Int,
               val type: Int,
               val age_rating: String?) : Parcelable {

        val getStatusText: String?
            get() {
                return Status(status).toString()
            }

        val getTypeText: String?
            get() {
                return Type(type).toString()
            }
    }

    @Parcelize
    data class Genre(val name: String) : Parcelable

    @Parcelize
    data class Wallpaper(val file: String) : Parcelable
}