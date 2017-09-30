package edu.jc.corsage.masterani.Masterani

import android.os.AsyncTask
import edu.jc.corsage.masterani.Adapters.SearchAdapter
import edu.jc.corsage.masterani.Masterani.Collection.Order
import edu.jc.corsage.masterani.Masterani.Collection.Status
import edu.jc.corsage.masterani.Masterani.Collection.Type
import edu.jc.corsage.masterani.Masterani.Entities.*
import edu.jc.corsage.masterani.Utils.SearchUtil
import edu.jc.corsage.masterani.Utils.WebUtil

/**
 * Created by j3chowdh on 9/9/2017.
 */

@Suppress("UNCHECKED_CAST")
class Masterani {
    val BASE_URL = "https://www.masterani.me/api/"
    val BASE_ANIME_URL = BASE_URL + "anime/"

    /* Filter URL Params
     * {order} - Required
     * {type} - Optional
     * {status} - Optional
     * {page} - Required
     */

    val BASE_FILTER_URL = BASE_ANIME_URL + "filter?order=%s"
    val FILTER_TYPE = "&type=%d"
    val FILTER_STATUS = "&status=%d"
    val FILTER_PAGE = "&page=%d"

    val RELEASES_URL = BASE_URL + "releases"
    val TRENDING_URL = BASE_ANIME_URL + "trending"

    val ANIME_INFO_URL = BASE_ANIME_URL + "%d"
    val DETAILED_ANIME_URL = BASE_ANIME_URL + "%d/detailed"

    /* Search URL Params
     * {search} - Required
     * {sb} - Optional [Note: SB stands for Search Bar, this causes results to be limited.]
     */
    val ANIME_SEARCH_URL = BASE_ANIME_URL + "search?search=%s&sb=true"


    /* Companion Objects
     * Uses a companion object for static uses.
     */

    // CDN -> Holds Masterani CDN info for image loading.
    object CDN {
        // TODO: Utilize the fact multiple res are available.

        // 3 - Lowest res wallpaper.
        @JvmStatic val WALLPAPER_URL = "https://cdn.masterani.me/wallpaper/2/"

        // 2 - 2nd lowest res poster.
        @JvmStatic val POSTER_URL = "https://cdn.masterani.me/poster/2/"

        // 1 - Highest res poster.
        @JvmStatic val POSTER_MAXSIZE_URL = "https://cdn.masterani.me/poster/1/"

        // Detailed Anime - Poster URL.
        @JvmStatic val DETAILED_POSTER_URL = "https://cdn.masterani.me/poster/"

        // Episode Thumbnail
        @JvmStatic val EPISODE_THUMBNAIL_URL = "https://cdn.masterani.me/episodes/"
    }

    /* Home Functions */
    fun getEpisodeReleases() : ArrayList<Episode> {
        return WebUtil("RELEASES_EPISODE")
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, RELEASES_URL)
                .get() as ArrayList<Episode>
    }

    fun getTrendingAnime() : Popular {
        return WebUtil("TRENDING_ANIME")
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, TRENDING_URL)
                .get() as Popular
    }

    fun getSpecificAnime(id: Int?) : DetailedAnime {
        return  WebUtil("ANIME")
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format(DETAILED_ANIME_URL, id))
                    .get() as DetailedAnime
    }

    fun getSpecificAnimeEpisodes(id: Int?) : DetailedAnimeEpisodes {
        return  WebUtil("ANIME_EPISODES")
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format(DETAILED_ANIME_URL, id))
                .get() as DetailedAnimeEpisodes
    }

    // Returns searched anime results.
    // TODO: Utilize AsyncTask.Status to check/end asyncTask before searching again.
    fun getSearchAnimeResults(adapter: SearchAdapter?, query: String)  {
        SearchUtil(adapter)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format(ANIME_SEARCH_URL, query))
    }

    /* Sort Functions */
    fun getSortedAnimes(order: Order, pageNumber: Int, type: Type? = null, status: Status? = null) : Sort {
        var url = String.format(BASE_FILTER_URL, order)

        if (type != null) {
            url += String.format(FILTER_TYPE, type.toString())
        }

        if (status != null) {
            url += String.format(FILTER_STATUS, status.toString())
        }

        url += String.format(FILTER_PAGE, pageNumber)

        return WebUtil("SORT").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url).get() as Sort
    }
}