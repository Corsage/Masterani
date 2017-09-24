package edu.jc.corsage.masterani.Sayonara

import android.os.AsyncTask
import android.util.Log
import edu.jc.corsage.masterani.Masterani.Entities.Episode
import edu.jc.corsage.masterani.Sayonara.Collection.Provider
import edu.jc.corsage.masterani.Sayonara.Entities.Link
import edu.jc.corsage.masterani.Utils.WebUtil

/**
 * Uses a custom API I created in node.js that finds links based on the ep.
 * Note: Due to IP_BOUND checks, the user has to scrape the actual mp4 if it exists.
 */

@Suppress("UNCHECKED_CAST")
class Sayonara(val slug: String, val episodeNumber: Int) {
    private val BASE_URL = "https://corsage-sayonara.herokuapp.com/"

    private val MASTERANI_VIDEO_API = "masterani/api/video"
    private val MASTERANI_EPISODE_LINKS = "?slug=%s&ep=%d"

    fun getLink() : String {
        // 1) Get the episode links.
        val links = getEpisodeLinks()

        // 2) Use an algorithm to find the best link for the user.
        val result = getBestLink(links)

        return result
    }

    private fun getBestLink(links : List<Link>) : String {
        /**
         * MP4Upload is first, it doesn't have IP_BOUND checks.
         */
        for (link: Link in links) {
            Log.d("Sayonara", link.name)
            if (link.id == Provider.getId(Provider.MP4UPLOAD)) {
                Log.d("Sayonara", "returning: " + link.link)
                return link.link
            }
        }

        return "lol"
    }

    private fun getEpisodeLinks() : List<Link> {
        return WebUtil("LINK")
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.format(BASE_URL + MASTERANI_VIDEO_API + MASTERANI_EPISODE_LINKS, slug, episodeNumber))
                .get() as ArrayList<Link>
    }
}