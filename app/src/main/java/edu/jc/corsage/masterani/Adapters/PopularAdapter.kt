package edu.jc.corsage.masterani.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.Masterani.Entities.Anime
import edu.jc.corsage.masterani.Masterani.Entities.Episode
import edu.jc.corsage.masterani.R

/**
 * Created by j3chowdh on 9/14/2017.
 */

class PopularAdapter(val context: Context, val listener: View.OnClickListener, val releases: List<Anime>) : RecyclerView.Adapter<PopularAdapter.PopularRowHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return releases.size
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): PopularRowHolder {
        val view = inflater.inflate(R.layout.item_popular, viewGroup, false)
        return PopularRowHolder(view)
    }

    override fun onBindViewHolder(prh: PopularRowHolder, pos: Int) {
        prh.view.tag = releases[pos].getID

        Glide.with(context)
                .load(releases[pos].getWallpaperURL)
                .apply(RequestOptions.placeholderOf(R.drawable.default_poster))
                .into(prh.ivAnimePoster)

        prh.tvAnimeRating.text = releases[pos].total.toString()
        prh.tvAnimeTitle.text = releases[pos].title
    }

    inner class PopularRowHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val ivAnimePoster: ImageView = view.findViewById(R.id.ivAnimePoster)
        val tvAnimeRating: TextView = view.findViewById(R.id.tvAnimeRating)
        val tvAnimeTitle: TextView = view.findViewById(R.id.tvAnimeTitle)

        init {
            view.setOnClickListener(listener)
        }
    }
}