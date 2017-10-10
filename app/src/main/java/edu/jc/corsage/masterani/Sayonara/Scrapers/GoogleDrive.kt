package edu.jc.corsage.masterani.Sayonara.Scrapers

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
import java.net.*

/**
 * Created by j3chowdh on 10/8/2017.
 */

class GoogleDrive(val cm: CookieManager) : AsyncTask<String, Void, String>() {
    private val BASE_URL: String = "https://docs.google.com"

    override fun doInBackground(vararg url: String): String {
        // TEST
        val conn = URL(url[0]).openConnection() as HttpURLConnection
        var data: String = ""

        data = conn.inputStream.bufferedReader().readText()
        conn.disconnect()

        Log.d("WebUtil", "[SUCCESS] Retrieved data.")

        // take the html and parse the link.
        var doc: Document = Jsoup.parse(data)
        val link: Element = doc.getElementById("uc-download-link")

        cm.put(URI(BASE_URL + link.attr("href")), conn.headerFields)

        return BASE_URL + link.attr("href")
    }
}