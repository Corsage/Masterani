package edu.jc.corsage.masterani.Fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import edu.jc.corsage.masterani.Adapters.ReleaseAdapter
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import kotlinx.android.synthetic.main.view_home.*
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import edu.jc.corsage.masterani.Adapters.PopularAdapter
import edu.jc.corsage.masterani.AnimeActivity
import java.lang.ref.WeakReference

/**
 * This fragment is the default fragment which will be shown when the application starts.
 * Offers the following:
 * 1) Recenty released anime episode.
 * 2) Current popular anime.
 * 3) Most watched anime.
 * 4) TODO: Favorites/Recently viewed...
 */

class HomeFragment : Fragment(), View.OnClickListener {
    private val masterani = Masterani()

    /* Newest Episodes */
    private var newestEps: RecyclerView? = null
    private var newestEpslayoutManager: LinearLayoutManager? = null

    private var episodeAdapter: ReleaseAdapter? = null

    private var epsSnapHelper = PagerSnapHelper()

    /* Trending Anime */
    private val getTodayStats = masterani.getTrendingAnime()

    private var beingWatched: RecyclerView? = null
    private var popularToday: RecyclerView? = null

    private var beingWatchedLayoutManager: GridLayoutManager? = null
    private var popularTodayLayoutManager: GridLayoutManager? = null

    private var beingWatchedAdapter: PopularAdapter? = null
    private var popularTodayAdapter: PopularAdapter? = null

    // Weak context for usage.
    var mWeakContext: WeakReference<Context>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_home, container, false)

        newestEps = view?.findViewById(R.id.releasesContainer)
        beingWatched = view?.findViewById(R.id.beingWatchedList)
        popularToday = view?.findViewById(R.id.popularTodayList)

        HomeUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        beingWatchedAdapter = PopularAdapter(context, this, getTodayStats.being_watched)
        popularTodayAdapter = PopularAdapter(context, this, getTodayStats.popular_today)

        // set onClickListeners to the arrows in releases containers..
        backBtn.setOnClickListener(this)
        forwardBtn.setOnClickListener(this)

        mWeakContext = WeakReference<Context>(context)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.backBtn -> {
                Log.d("hehexd", newestEpslayoutManager!!.findFirstVisibleItemPosition().toString())
                releasesContainer.smoothScrollToPosition(newestEpslayoutManager!!.findFirstVisibleItemPosition() - 1)
            }

            R.id.forwardBtn -> {
                releasesContainer.smoothScrollToPosition(newestEpslayoutManager!!.findLastVisibleItemPosition() + 1)
            }

            R.id.searchItem, R.id.popularMain -> {
                // Only id is needed to create url.
                val id = view.tag as Int
                val intent = Intent(context, AnimeActivity::class.java)
                intent.putExtra("ID", id)
                context.startActivity(intent)
            }
        }
    }

     // HomeUtil AsyncTask
     // Used to load information onto fragment without making the user wait for the UI to load all at once.
    inner class HomeUtil : AsyncTask<Int?, Unit, Unit>() {
        override fun doInBackground(vararg p0: Int?) {
            newestEpslayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            episodeAdapter = ReleaseAdapter(context, masterani.getEpisodeReleases())

            beingWatchedLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)

            popularTodayLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        }

         override fun onPostExecute(result: Unit?) {
             newestEps?.layoutManager = newestEpslayoutManager
             newestEps?.adapter = episodeAdapter
             epsSnapHelper.attachToRecyclerView(newestEps)

             beingWatched?.layoutManager = beingWatchedLayoutManager
             beingWatched?.adapter = beingWatchedAdapter

             popularToday?.layoutManager = popularTodayLayoutManager
             popularToday?.adapter = popularTodayAdapter
         }
    }
}

