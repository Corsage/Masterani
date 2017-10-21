package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import edu.jc.corsage.masterani.Masterani.Collection.Status
import edu.jc.corsage.masterani.Masterani.Collection.Type
import edu.jc.corsage.masterani.Masterani.Masterani
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * TODO: Refactor this piece of shit.
 */

@Parcelize
class DetailedAnimeEpisodes(val info: InfoEpisode, val episodes: List<DetailedEpisode>) : Parcelable

@Parcelize
class InfoEpisode(val slug: String, val episode_count: Int?, val episode_length: Int?): Parcelable

@Parcelize
class AnimeSearch(val id: Int,
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
class SortAnime(val id: Int,
                val title: String,
                val slug: String,
                val status: Int?,
                val type: Int?,
                val score: Float?,
                val poster: Poster?) : Parcelable {
    @Parcelize
    class Poster(val file: String?) : Parcelable {
        val getPoster: String?
        get() {
            if (file != null) {
                return Masterani.CDN.POSTER_URL + file
            } else {
                return null
            }
        }
    }
}

@Parcelize
class DetailedAnime(val info: Info,
                    val poster: String? = null,
                    val genres: List<Genre>?,
                    val wallpapers: List<Wallpaper>?) : Parcelable {

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
            if (wallpapers!!.isNotEmpty()) {
                return Masterani.CDN.WALLPAPER_URL + wallpapers[Random().nextInt(wallpapers.size)].file
            } else {
                return null
            }
        }

    @Parcelize
    class Info(val id: Int,
               val title: String,
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