package edu.jc.corsage.masterani.Utils

import android.os.AsyncTask
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import edu.jc.corsage.masterani.Masterani.Collection.Order
import edu.jc.corsage.masterani.Masterani.Entities.*
import edu.jc.corsage.masterani.Sayonara.Entities.Link
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

/**
 * Created by j3chowdh on 9/9/2017.
 */

@Suppress("UNCHECKED_CAST")
class WebUtil(val object_type: String) : AsyncTask<String, Void, Any?>() {
    private val moshi = Moshi.Builder().build()

    override fun onPreExecute() {
        //super.onPreExecute()
    }

    private fun parse(data: String) : Any? {
        val type: ParameterizedType?
        val adapter: Any?

        when (object_type.toUpperCase()) {
            // Releases.
            "RELEASES_EPISODE" -> {
                type = Types.newParameterizedType(List::class.java, Episode::class.java)
                adapter = moshi.adapter<JsonAdapter<List<Episode>>>(type)
                return adapter.fromJson(data)
            }

            // Trending.
            "TRENDING_ANIME" -> {
                type = Types.newParameterizedType(Popular::class.java, List::class.java, Anime::class.java)
                adapter = moshi.adapter<JsonAdapter<Popular>>(type)
                return adapter.fromJson(data)
            }

            // Specific anime.
            "ANIME" -> {
                type = Types.newParameterizedType(DetailedAnime::class.java)
                adapter = moshi.adapter<DetailedAnime>(type)
                return adapter.fromJson(data)
            }

            // Links.
            "LINK" -> {
                type = Types.newParameterizedType(List::class.java, Link::class.java)
                adapter = moshi.adapter<JsonAdapter<List<Link>>>(type)
                return adapter.fromJson(data)
            }

            // Sorting.
            "SORT" -> {
                type = Types.newParameterizedType(Sort::class.java, List::class.java)
                adapter = moshi.adapter<Sort>(type)
                return adapter.fromJson(data)
            }

            else -> {
                Log.d("WebUtil", "Unknown type: " + object_type)
                return null
            }
        }
    }

    override fun doInBackground(vararg url: String): Any? {
        val conn = URL(url[0]).openConnection() as HttpURLConnection
        var data: String = ""

        try {
            Log.d("WebUtil", "Trying to connect to: " + url[0])
            data = conn.inputStream.bufferedReader().readText()
        } catch(e : IOException) {
            Log.d("WebUtil", "[ERROR] Network Error: " + e.toString())
        } finally {
            conn.disconnect()
        }
        Log.d("WebUtil", "[SUCCESS] Retrieved data.")

        /* Parse JSON object */
        return parse(data)
    }

    override fun onPostExecute(result: Any?) {
        //super.onPostExecute(result)
    }
}