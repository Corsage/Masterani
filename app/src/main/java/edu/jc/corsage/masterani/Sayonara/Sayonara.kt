package edu.jc.corsage.masterani.Sayonara

import android.os.AsyncTask
import android.util.Log
import edu.jc.corsage.masterani.Sayonara.Collection.Provider
import edu.jc.corsage.masterani.Sayonara.Entities.Link
import edu.jc.corsage.masterani.Sayonara.Entities.Schedule
import edu.jc.corsage.masterani.Sayonara.Scrapers.GoogleDrive
import edu.jc.corsage.masterani.Sayonara.Scrapers.StreamMoe
import edu.jc.corsage.masterani.Utils.WebUtil
import java.net.CookieManager
import java.net.CookiePolicy

/**
 * Uses a custom API I created in node.js that finds links based on the ep.
 * Note: Due to IP_BOUND checks, the user has to scrape the actual mp4 if it exists.
 */

@Suppress("UNCHECKED_CAST")
class Sayonara {
    private val BASE_URL = "https://corsage-sayonara.herokuapp.com/"

    private val MASTERANI_VIDEO_API = "masterani/api/video"
    private val MASTERANI_SCHEDULE_API = "masterani/api/schedule"

    private val MASTERANI_EPISODE_LINKS = "?slug=%s&ep=%d"

    // Cookie Manager.
    object CM {
        @JvmStatic val cm: CookieManager = CookieManager()

        init {
            cm.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER)
        }
    }

    fun getLink(slug: String, episodeNumber: Int) : String {
        // 1) Get the episode links.
        val links = getEpisodeLinks(slug, episodeNumber)

        // 2) Use an algorithm to find the best link for the user.
        val result = getBestLink(links)

        return result
    }

    /* Schedule */
    fun getSchedule() : List<Schedule> {
        return WebUtil("ANIME_SCHEDULE")
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, BASE_URL + MASTERANI_SCHEDULE_API)
                .get() as List<Schedule>
    }

    private fun getBestLink(links : List<Link>) : String {
        /**
         * MP4Upload is first, it doesn't have IP_BOUND checks.
         */
        for (provider: Link in links) {
            Log.d("Sayonara", "Link name: " + provider.name)

            when (provider.id) {
                Provider.getId(Provider.MASTERANI), Provider.getId(Provider.MP4UPLOAD) -> {
                    return provider.link
                }

                Provider.getId(Provider.GDRIVE) -> {
                    return GoogleDrive(CM.cm).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, provider.link).get()
                }

                Provider.getId(Provider.STREAMMOE) -> {
                    return StreamMoe().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, provider.link).get()
                }

            }
        }

        return "lol"
    }

    private fun getEpisodeLinks(slug: String, episodeNumber: Int) : List<Link> {
        return WebUtil("LINK")
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format(BASE_URL + MASTERANI_VIDEO_API + MASTERANI_EPISODE_LINKS, slug, episodeNumber))
                .get() as ArrayList<Link>
    }
}