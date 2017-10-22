package edu.jc.corsage.masterani.Fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v4.widget.SwipeRefreshLayout
import edu.jc.corsage.masterani.Adapters.ReleaseAdapter
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import kotlinx.android.synthetic.main.view_home.*
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import edu.jc.corsage.masterani.Adapters.PopularAdapter
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.Masterani.Entities.Anime
import edu.jc.corsage.masterani.Masterani.Entities.Popular
import java.lang.ref.WeakReference

/**
 * This fragment is the default fragment which will be shown when the application starts.
 * Offers the following:
 * 1) Recenty released anime episode.
 * 2) Current popular anime.
 * 3) Most watched anime.
 * 4) TODO: Favorites/Recently viewed...
 */

class HomeFragment : Fragment(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private val masterani = Masterani()
    private val parent = this
    private var activity: Context? = null

    /* SwipeRefreshLayout */
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    /* Newest Episodes */
    private var newestEps: RecyclerView? = null
    private var newestEpslayoutManager: LinearLayoutManager? = null

    private var episodeAdapter: ReleaseAdapter? = null

    private var epsSnapHelper = PagerSnapHelper()

    /* Trending Anime */
    private var getTodayStats: Popular? = null

    private var beingWatched: RecyclerView? = null
    private var popularToday: RecyclerView? = null

    private var beingWatchedLayoutManager: GridLayoutManager? = null
    private var popularTodayLayoutManager: GridLayoutManager? = null

    private var beingWatchedAdapter: PopularAdapter? = null
    private var popularTodayAdapter: PopularAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if recreating instance.
        if (savedInstanceState != null) {
            episodeAdapter = ReleaseAdapter(context, savedInstanceState.getParcelableArrayList("newestEps"))
            beingWatchedAdapter = PopularAdapter(context, this, savedInstanceState.getParcelableArrayList("beingWatched"))
            popularTodayAdapter = PopularAdapter(context, this, savedInstanceState.getParcelableArrayList("popularToday"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_home, container, false)

        newestEps = view?.findViewById(R.id.releasesContainer)
        beingWatched = view?.findViewById(R.id.beingWatchedList)
        popularToday = view?.findViewById(R.id.popularTodayList)

        swipeRefreshLayout = view?.findViewById(R.id.homeRefresh)

        if (episodeAdapter == null ||  beingWatchedAdapter == null || popularTodayAdapter == null) {
            Log.d("HomeFragment", "Adapters/Layout Managers are null.")
            HomeUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            // Re-visiting fragment.
            if (newestEpslayoutManager == null) {
                newestEpslayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            newestEps?.layoutManager = newestEpslayoutManager

            if (beingWatchedLayoutManager == null) {
                beingWatchedLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            }
            beingWatched?.layoutManager = beingWatchedLayoutManager

            if (popularTodayLayoutManager == null) {
                popularTodayLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            }
            popularToday?.layoutManager = popularTodayLayoutManager
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (newestEps?.adapter == null) {
            newestEps?.adapter = episodeAdapter
        }

        if (beingWatched?.adapter == null) {
            beingWatched?.adapter = beingWatchedAdapter
        }

        if (popularToday?.adapter == null) {
            popularToday?.adapter = popularTodayAdapter
        }
        epsSnapHelper.attachToRecyclerView(newestEps)

        // set onClickListeners to the arrows in releases containers..
        backBtn.setOnClickListener(this)
        forwardBtn.setOnClickListener(this)

        // Refresh Listener
        swipeRefreshLayout?.setOnRefreshListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("newestEps", episodeAdapter?.releases)
        outState?.putParcelableArrayList("popularToday", popularTodayAdapter?.releases)
        outState?.putParcelableArrayList("beingWatched", beingWatchedAdapter?.releases)
    }

    override fun onDestroy() {
        newestEpslayoutManager = null
        popularTodayLayoutManager = null
        beingWatchedLayoutManager = null
        super.onDestroy()
    }

    override fun onAttach(context: Context?) {
        activity = context
        super.onAttach(context)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.backBtn -> {
                if (newestEpslayoutManager!!.findFirstVisibleItemPosition() > 0) {
                    releasesContainer.smoothScrollToPosition(newestEpslayoutManager!!.findFirstVisibleItemPosition() - 1)
                }
            }

            R.id.forwardBtn -> {
                if (newestEpslayoutManager!!.findLastVisibleItemPosition() < newestEpslayoutManager!!.itemCount) {
                    releasesContainer.smoothScrollToPosition(newestEpslayoutManager!!.findLastVisibleItemPosition() + 1)
                }
            }

            R.id.searchItem, R.id.popularMain -> {
                // Only id is needed to create url.
                val id = view.tag as Int
                val intent = Intent(activity, AnimeActivity::class.java)
                intent.putExtra("ID", id)
                activity?.startActivity(intent)
            }
        }
    }

    override fun onRefresh() {
        HomeUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

     // HomeUtil AsyncTask
     // Used to load information onto fragment without making the user wait for the UI to load all at once.
    inner class HomeUtil : AsyncTask<Int?, Unit, Unit>() {
         override fun onPreExecute() {
             swipeRefreshLayout!!.isRefreshing = true
         }

        override fun doInBackground(vararg p0: Int?) {
            getTodayStats = masterani.getTrendingAnime()

            newestEpslayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            episodeAdapter = ReleaseAdapter(context, masterani.getEpisodeReleases())

            beingWatchedLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            beingWatchedAdapter = PopularAdapter(context, parent, getTodayStats!!.being_watched as ArrayList<Anime>)

            popularTodayLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            popularTodayAdapter = PopularAdapter(context, parent, getTodayStats!!.popular_today as ArrayList<Anime>)
        }

         override fun onPostExecute(result: Unit?) {
             newestEps?.layoutManager = newestEpslayoutManager
             newestEps?.adapter = episodeAdapter

             beingWatched?.layoutManager = beingWatchedLayoutManager
             beingWatched?.adapter = beingWatchedAdapter

             popularToday?.layoutManager = popularTodayLayoutManager
             popularToday?.adapter = popularTodayAdapter

             swipeRefreshLayout!!.isRefreshing = false
         }
    }
}

