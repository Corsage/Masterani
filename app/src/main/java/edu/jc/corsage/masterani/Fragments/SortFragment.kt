package edu.jc.corsage.masterani.Fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridView
import edu.jc.corsage.masterani.Adapters.AnimeAdapter
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.Masterani.Collection.Order
import edu.jc.corsage.masterani.Masterani.Entities.SortAnime
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import java.util.ArrayList

/**
 * This fragment is shown when the user selects the sort menu item.
 * Default view will show all animes A-Z.
 */

class SortFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener, AbsListView.OnScrollListener {
    private var pageNumber = 1
    private var totalItems: Int? = null
    private var lastPageNumber: Int? = null

    private var masterani: Masterani? = null
    private var animeList: GridView? = null
    private var animeAdapter: AnimeAdapter? = null

    private var sortLoading: ContentLoadingProgressBar? = null

    private var loading: Boolean = false

    // TEST
    private var orderButton: Button? = null
    private var typeButton: Button? = null
    private var statusButton: Button? = null

    // Lol'd
    private val orderArray = arrayOf("Score - High", "Score - Low", "Title A - Z", "Title Z - A")
    private val typeArray = arrayOf("All", "TV", "Movie", "OVA", "Special", "ONA")
    private val statusArray = arrayOf("All", "Completed", "Ongoing", "Not Yet Aired")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if recreating instance.
        if (savedInstanceState != null) {
            animeAdapter = AnimeAdapter(context, savedInstanceState.getParcelableArrayList("SortedAnimes"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_sort, container, false)
        animeList = view?.findViewById(R.id.animeList)
        sortLoading = view?.findViewById(R.id.sortLoading)

        orderButton = view?.findViewById(R.id.sortSort)
        typeButton = view?.findViewById(R.id.sortType)
        statusButton = view?.findViewById(R.id.sortStatus)

        if (animeAdapter == null || animeAdapter!!.isEmpty) {
            SortUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            animeList?.adapter = animeAdapter
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set button listeners.
        orderButton?.setOnClickListener(this)
        typeButton?.setOnClickListener(this)
        statusButton?.setOnClickListener(this)

        // Set onScrollListener
        animeList?.setOnScrollListener(this)
        animeList?.onItemClickListener = this
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("SortedAnimes", animeAdapter?.animeList as ArrayList<SortAnime>)
    }

    /* OnItemClick Listener */
    override fun onItemClick(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
        if (view?.id == R.id.SortAnime) {
            // Only id is needed to create url.
            val id = view.tag as Int
            val intent = Intent(activity, AnimeActivity::class.java)
            intent.putExtra("ID", id)
            activity?.startActivity(intent)
        }
    }

    /* OnClick Listener */
    override fun onClick(view: View?) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)

        when (view?.id) {
            R.id.sortSort -> {
                builder.setTitle("Order")
                        .setItems(orderArray, DialogInterface.OnClickListener {
                            dialogInterface, i -> Log.d("SortFragment", "selected " + i.toString())
                        })
            }

            R.id.sortType -> {
                builder.setTitle("Type")
                        .setItems(typeArray, DialogInterface.OnClickListener {
                            dialogInterface, i -> Log.d("SortFragment", "selected " + i.toString())
                        })
            }

            R.id.sortStatus -> {
                builder.setTitle("Status")
                        .setItems(statusArray, DialogInterface.OnClickListener {
                            dialogInterface, i -> Log.d("SortFragment", "selected " + i.toString())
                        })
            }

        }

        builder.create()
        builder.show()
    }

    /* OnScroll Listener */
    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        // Check if the last item is visible or not.
        if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
            // Check if we're already on the last page.
            if ((lastPageNumber != null) && (pageNumber <= lastPageNumber as Int) && (!loading)) {
                pageNumber++
                Log.d("SortFragment", "Getting more items.")
                LoadMoreItems().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get()
            }
        }
    }

    override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
        //...
    }

    inner class LoadMoreItems : AsyncTask<Int?, Unit, Unit>() {
        override fun onPreExecute() {
            loading = true
        }
        override fun doInBackground(vararg p0: Int?) {
            if (masterani == null) {
                masterani = Masterani()
            }

            val temp = masterani?.getSortedAnimes(Order.SCORE_HIGH, pageNumber)

            if (animeAdapter != null) {
                animeAdapter?.addItems(temp!!.data as ArrayList<SortAnime>)
            }

            lastPageNumber = temp?.last_page
        }

        override fun onPostExecute(result: Unit?) {
            totalItems = animeAdapter?.count
            animeAdapter?.notifyDataSetChanged()
            loading = false
        }
    }

    // SortUtil AsyncTask
    inner class SortUtil : AsyncTask<Int?, Unit, Unit>() {
        override fun onPreExecute() {
            sortLoading?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: Int?) {
            if (masterani == null) {
                masterani = Masterani()
            }

            val temp = masterani?.getSortedAnimes(Order.SCORE_HIGH, pageNumber)
            animeAdapter = AnimeAdapter(context, temp!!.data as ArrayList<SortAnime>)
            lastPageNumber = temp.last_page
        }

        override fun onPostExecute(result: Unit?) {
            sortLoading?.visibility = View.INVISIBLE
            animeList?.adapter = animeAdapter
            totalItems = animeAdapter?.count
        }
    }
}