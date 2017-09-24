package edu.jc.corsage.masterani.Fragments

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.widget.GridView
import edu.jc.corsage.masterani.Adapters.ReleaseAdapter
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import kotlinx.android.synthetic.main.view_home.*
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import edu.jc.corsage.masterani.Adapters.AnimeAdapter
import edu.jc.corsage.masterani.Adapters.PopularAdapter
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.Masterani.Collection.Order
import edu.jc.corsage.masterani.Sayonara.Sayonara
import edu.jc.corsage.masterani.WatchActivity
import java.lang.ref.WeakReference


/**
 * This fragment is the default fragment which will be shown when the application starts.
 * Offers the following:
 * 1) Recenty released anime episode.
 * 2) Current popular anime.
 * 3) Most watched anime.
 */

class HomeFragment() : Fragment(), View.OnClickListener {
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

        /* Newest Episodes */
        newestEps = view?.findViewById(R.id.releasesContainer)

        newestEpslayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        newestEps?.layoutManager = newestEpslayoutManager

        episodeAdapter = ReleaseAdapter(context, masterani.getEpisodeReleases())
        newestEps?.adapter = episodeAdapter

        epsSnapHelper.attachToRecyclerView(newestEps)

        /* Trending Anime */

        // Being Watched
        beingWatched = view?.findViewById(R.id.beingWatchedList)

        beingWatchedLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        beingWatched?.layoutManager = beingWatchedLayoutManager

        beingWatchedAdapter = PopularAdapter(context, this, getTodayStats.being_watched)
        beingWatched?.adapter = beingWatchedAdapter

        // Popular Today
        popularToday = view?.findViewById(R.id.popularTodayList)

        popularTodayLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        popularToday?.layoutManager = popularTodayLayoutManager

        popularTodayAdapter = PopularAdapter(context, this, getTodayStats.popular_today)
        popularToday?.adapter = popularTodayAdapter

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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
}

