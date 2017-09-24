package edu.jc.corsage.masterani.Adapters

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import edu.jc.corsage.masterani.Masterani.Entities.Episode

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.placeholderOf
import com.bumptech.glide.request.RequestOptions.centerCropTransform
import edu.jc.corsage.masterani.Fragments.HomeFragment
import edu.jc.corsage.masterani.MainActivity
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Sayonara
import edu.jc.corsage.masterani.WatchActivity
import java.lang.ref.WeakReference

/**
 * Created by j3chowdh on 9/10/2017.
 */

class ReleaseAdapter(val context: Context, val releases: ArrayList<Episode>) : RecyclerView.Adapter<ReleaseAdapter.ReleaseRowHolder>(), View.OnClickListener {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return releases.size
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun onClick(view: View) {
        val info = view.tag as Episode

        Log.d("onClick", "you clicked me.")

        ShowEpisode(info.anime!!.slug, info.episode!!.toInt()).execute()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): ReleaseRowHolder {
        val view = inflater.inflate(R.layout.item_release, viewGroup, false)
        return ReleaseRowHolder(view)
    }

    override fun onBindViewHolder(rrh: ReleaseRowHolder, pos: Int) {
        rrh.view.tag = releases[pos]

        Glide.with(context)
                .load(releases[pos].anime?.getWallpaperURL)
                //.apply(placeholderOf(R.drawable.default_poster))
                .apply(centerCropTransform())
                .into(rrh.releasePoster)

        rrh.releaseTime.text = releases[pos].created_at
        rrh.animeTitle.text = releases[pos].anime?.title

        // TEST
        rrh.episodeNumber.text = "EP. " + releases[pos].episode

        rrh.view.setOnClickListener(this)
    }

    // Handling actions.
    inner class ShowEpisode(val slug: String, val episode: Int) : AsyncTask<Void, Intent?, Intent?>() {
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            // Start the progress bar.
            progressDialog = ProgressDialog.show(context, "Masterani", "Loading episode...")
        }

        override fun doInBackground(vararg p0: Void?): Intent? {
            // Episode, start to video.
            val sayonara = Sayonara(slug, episode).getLink()

            val intent = Intent(context, WatchActivity::class.java)
            intent.putExtra("URL", sayonara)

            return intent
        }

        override fun onPostExecute(result: Intent?) {
            progressDialog?.dismiss()
            context.startActivity(result)
        }
    }

     inner class ReleaseRowHolder(val view: View) : RecyclerView.ViewHolder(view) {
         val releasePoster: ImageView = view.findViewById(R.id.releasePoster)
         val releaseTime: TextView = view.findViewById(R.id.releaseTime)
         val animeTitle: TextView = view.findViewById(R.id.animeTitle)
         val episodeNumber: TextView = view.findViewById(R.id.episodeNumber)
    }
}