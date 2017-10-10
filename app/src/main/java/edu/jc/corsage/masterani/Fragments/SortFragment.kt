package edu.jc.corsage.masterani.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import edu.jc.corsage.masterani.Adapters.AnimeAdapter
import edu.jc.corsage.masterani.Adapters.ReleaseAdapter
import edu.jc.corsage.masterani.Masterani.Collection.Order
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R

/**
 * This fragment is shown when the user selects the sort menu item.
 * Default view will show all animes A-Z.
 * TODO: UI Design for sort...
 */

class SortFragment() : Fragment() {
    private var pageNumber = 1

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_sort, container, false)

        val masterani = Masterani()

       // val www = view?.findViewById<GridView>(R.id.animeList)

        // TEST
        //val xd = AnimeAdapter(context, masterani.getSortedAnimes(Order.SCORE_HIGH, pageNumber).data)

       // www?.adapter = xd
        Log.d("SortFragment", "Creating view")
        return view
    }
}