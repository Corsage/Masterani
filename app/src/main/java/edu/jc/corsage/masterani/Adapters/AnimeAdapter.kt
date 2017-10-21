package edu.jc.corsage.masterani.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.Masterani.Entities.SortAnime
import edu.jc.corsage.masterani.R

/**
 * Created by j3chowdh on 9/10/2017.
 */

public class AnimeAdapter(val context: Context, val animeList: ArrayList<SortAnime>) : BaseAdapter() {
    private val inflator: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return animeList.size
    }

    override fun getItem(pos: Int): Any {
        return animeList[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    fun addItems(items: ArrayList<SortAnime>) {
        this.animeList.addAll(items)
    }

    override fun getView(pos: Int, view: View?, viewGroup: ViewGroup): View {
        val v: View

        if (view == null) {
            v = this.inflator.inflate(R.layout.item_anime, viewGroup, false)
        } else {
            v = view
        }
        v.tag = animeList[pos].id
        val arh = AnimeRowHolder(v)

        Glide.with(context)
               .load(animeList[pos].poster?.getPoster)
                .apply(RequestOptions.placeholderOf(R.drawable.default_poster))
                .into(arh.ivAnimePoster)

        arh.tvAnimeRating.text = animeList[pos].score.toString()
        arh.tvAnimeTitle.text = animeList[pos].title
        arh.tvAnimeInfo.text = "test"

        return v
    }

    private class AnimeRowHolder(view: View) {
        val ivAnimePoster: ImageView
        val tvAnimeRating: TextView
        val tvAnimeTitle: TextView
        val tvAnimeInfo: TextView

        init {
            this.ivAnimePoster = view.findViewById(R.id.ivAnimePoster)
            this.tvAnimeRating = view.findViewById(R.id.tvAnimeRating)
            this.tvAnimeTitle = view.findViewById(R.id.tvAnimeTitle)
            this.tvAnimeInfo = view.findViewById(R.id.tvAnimeInfo)
        }
    }
}