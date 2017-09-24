package edu.jc.corsage.masterani.Utils

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.Masterani.Entities.DetailedAnime
import java.lang.ref.WeakReference

/**
 * Created by j3chowdh on 9/23/2017.
 */

@Suppress("UNCHECKED_CAST")
class AnimeUtil(private val mWeakContext: WeakReference<Context>?) : AsyncTask<DetailedAnime, Void, Intent>() {
    private val EXTRA_MESSAGE = "ANIME"

    override fun onPreExecute() {

    }

    override fun doInBackground(vararg animes: DetailedAnime): Intent {
        // Create intent.
        val intent = Intent(mWeakContext?.get(), AnimeActivity::class.java)
        intent.putExtra(EXTRA_MESSAGE, animes[0])

        return intent
    }

    override fun onPostExecute(result: Intent) {
        mWeakContext?.get()?.startActivity(result)
    }
}