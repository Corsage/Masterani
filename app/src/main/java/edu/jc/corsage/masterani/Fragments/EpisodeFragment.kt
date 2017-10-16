package edu.jc.corsage.masterani.Fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.Adapters.EpisodeAdapter
import edu.jc.corsage.masterani.Masterani.Entities.DetailedEpisode
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Sayonara
import edu.jc.corsage.masterani.WatchActivity
import kotlinx.android.synthetic.main.view_episode.*

/**
 * Created by j3chowdh on 9/23/2017.
 */

class EpisodeFragment : Fragment(), AdapterView.OnItemClickListener {
    private lateinit var episodes: List<DetailedEpisode>
    private lateinit var slug: String

    private var episode_length: Int? = null

    private lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_episode, container, false)
        EpisodeUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arguments.getInt("ID"))

        return view
    }

    // Wait until view is created in order to populate listview.
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        episodeListView.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
        val info = v?.tag as DetailedEpisode.Info?

        ShowEpisode(slug, info?.episode!!.toInt()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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

            // TEST NEXT EPISODE IMPLEMENTATION
            intent.putExtra("SLUG", slug)
            intent.putExtra("CURRENT_EPISODE", episode)
            intent.putExtra("EPISODE_COUNT", episodeAdapter.count)

            return intent
        }

        override fun onPostExecute(result: Intent?) {
            progressDialog?.dismiss()
            context.startActivity(result)
        }
    }

    inner class EpisodeUtil : AsyncTask<Int?, Unit, Unit>() {
        override fun doInBackground(vararg p0: Int?) {
            val temp = Masterani().getSpecificAnimeEpisodes(p0[0])
            episodes = temp.episodes
            slug = temp.info.slug
            episode_length = temp.info.episode_length
        }

        override fun onPostExecute(result: Unit?) {
            episodeAdapter = EpisodeAdapter(context, episodes, episode_length)
            episodeListView.adapter = episodeAdapter
        }
    }
}