package edu.jc.corsage.masterani.Utils

import android.database.MatrixCursor
import android.os.AsyncTask
import android.provider.BaseColumns
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import edu.jc.corsage.masterani.Adapters.SearchAdapter
import edu.jc.corsage.masterani.Masterani.Entities.*
import edu.jc.corsage.masterani.Sayonara.Entities.Link
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by j3chowdh on 9/21/2017.
 */

@Suppress("UNCHECKED_CAST")
class SearchUtil(val adapter: SearchAdapter?) : AsyncTask<String, Void, MatrixCursor>() {
    private val moshi = Moshi.Builder().build()
    private val columns = arrayOf(BaseColumns._ID, "title", "info")

    override fun onPreExecute() {
        //super.onPreExecute()
    }

    override fun doInBackground(vararg url: String): MatrixCursor {
        val conn = URL(url[0]).openConnection() as HttpURLConnection
        var data: String = ""

        val type: ParameterizedType?
        val adapter: Any?

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
        type = Types.newParameterizedType(List::class.java, AnimeSearch::class.java)
        adapter = moshi.adapter<JsonAdapter<List<AnimeSearch>>>(type)
         //adapter.fromJson(data)

        // Create cursor.
        val cursor = MatrixCursor(columns)

        // Retrieve search results.
        val suggestions = adapter.fromJson(data) as List<AnimeSearch>

        for (i in suggestions.indices) {
            val tmp = arrayOf(suggestions[i].id, suggestions[i].title, suggestions[i].getInfoText)
            cursor.addRow(tmp)
        }

        return cursor
    }

    override fun onPostExecute(result: MatrixCursor) {
        adapter?.swapCursor(result)
    }
}