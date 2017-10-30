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
import android.widget.ListView
import edu.jc.corsage.masterani.Adapters.EpisodeAdapter
import edu.jc.corsage.masterani.Masterani.Entities.DetailedEpisode
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Sayonara
import edu.jc.corsage.masterani.WatchActivity
import java.util.ArrayList

/**
 * Created by j3chowdh on 9/23/2017.
 */

class EpisodeFragment : Fragment(), AdapterView.OnItemClickListener {
    private var slug: String? = null
    private var episode_length: Int? = null

    private var episodeListView: ListView? = null
    private var episodeAdapter: EpisodeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if recreating instance.
        if (savedInstanceState != null) {
            episode_length = savedInstanceState.getInt("EPISODE_LENGTH")
            slug = savedInstanceState.getString("SLUG")
            episodeAdapter = EpisodeAdapter(context, savedInstanceState.getParcelableArrayList("EPISODES"), episode_length)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_episode, container, false)

        episodeListView = view.findViewById(R.id.episodeListView)

        if (episodeAdapter == null) {
            EpisodeUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arguments.getInt("ID"))
        } else {
            episodeListView?.adapter = episodeAdapter
        }

        return view
    }

    // Wait until view is created in order to populate listview.
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        episodeListView?.onItemClickListener = this
    }

    override fun onItemClick(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
        val info = v?.tag as DetailedEpisode.Info?

        ShowEpisode(slug as String, info?.episode!!.toInt()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        if (outState != null) {
            if (!outState.containsKey("EPISODES")) {
                outState.putParcelableArrayList("EPISODES", episodeAdapter?.episodeList as ArrayList<DetailedEpisode>)
            }
            if (!outState.containsKey("EPISODE_LENGTH")) {
                outState.putInt("EPISODE_LENGTH", episode_length as Int)
            }
            if (!outState.containsKey("SLUG")) {
                outState.putString("SLUG", slug)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("EpisodeFragment", "Destroying...")

        slug = null
        episode_length = null
        episodeListView = null
        episodeAdapter = null
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
            val sayonara = Sayonara().getLink(slug, episode)

            val intent = Intent(context, WatchActivity::class.java)
            intent.putExtra("URL", sayonara)

            // TEST NEXT EPISODE IMPLEMENTATION
            intent.putExtra("SLUG", slug)
            intent.putExtra("CURRENT_EPISODE", episode)
            intent.putExtra("EPISODE_COUNT", episodeAdapter?.count)

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
            slug = temp.info.slug
            episode_length = temp.info.episode_length

            episodeAdapter = EpisodeAdapter(context, temp.episodes, episode_length)
        }

        override fun onPostExecute(result: Unit?) {
            episodeListView?.adapter = episodeAdapter
        }
    }
}