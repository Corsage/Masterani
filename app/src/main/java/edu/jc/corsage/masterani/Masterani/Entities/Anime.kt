package edu.jc.corsage.masterani.Masterani.Entities

import android.os.Parcelable
import android.util.Log
import edu.jc.corsage.masterani.Masterani.Collection.Type
import edu.jc.corsage.masterani.Masterani.Masterani
import kotlinx.android.parcel.Parcelize

/**
 * Anime data class.
 * Note: First constructor is based off of releases API, which is the most minimal.
 */

@Parcelize
class Anime(val slug: String) : Parcelable {
    var total: Int? = -1
    var id: Int? = -1

    var title: String = "UNKNOWN"

    var duration: Int? = -1

    private var poster: String? = "UNKNOWN"
    private var wallpaper: String? = "UNKNOWN"

    val getID: Int?
        get() {
            if (id == null) {
                id = Integer.parseInt(slug.substring(0, slug.indexOf('-')))
            }

            return id
        }

    val getWallpaperURL: String
        get() {
            if (wallpaper != null) {
                return Masterani.CDN.WALLPAPER_URL + wallpaper
            } else {
                return Masterani.CDN.POSTER_MAXSIZE_URL + poster
            }
        }
}