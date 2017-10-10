package edu.jc.corsage.masterani.Sayonara.Scrapers

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.CookieManager

/**
 * Created by j3chowdh on 10/9/2017.
 */

class StreamMoe : AsyncTask<String, Void, String>() {
    override fun doInBackground(vararg url: String): String {
       val doc: Document = Jsoup.connect(url[0]).get()
        val table: Element = doc.select("table.accountStateTable td:eq(1) a")[0]

        Log.d("Stream.moe", "link: " + table.attr("href"))

        return table.attr("href")
    }
}