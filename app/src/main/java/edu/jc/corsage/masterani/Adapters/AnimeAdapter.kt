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
import edu.jc.corsage.masterani.Masterani.Entities.DetailedAnime
import edu.jc.corsage.masterani.R

/**
 * Created by j3chowdh on 9/10/2017.
 */

public class AnimeAdapter(val context: Context, animeList: List<DetailedAnime>) : BaseAdapter() {
    private val inflator: LayoutInflater = LayoutInflater.from(context)
    private val animeList: List<DetailedAnime> = animeList

    override fun getCount(): Int {
        return animeList.size
    }

    override fun getItem(pos: Int): Any {
        return animeList[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(pos: Int, view: View?, viewGroup: ViewGroup): View {
        val v: View

        if (view == null) {
            v = this.inflator.inflate(R.layout.item_anime, viewGroup, false)
        } else {
            v = view
        }
        v.tag = animeList[pos]
        val arh = AnimeRowHolder(v)

        //Glide.with(context)
               // .load(animeList[pos].getPosterURL)
                //.apply(RequestOptions.placeholderOf(R.drawable.default_poster))
                //.into(arh.ivAnimePoster)

       // arh.tvAnimeRating.text = animeList[pos].score.toString()
       // arh.tvAnimeTitle.text = animeList[pos].title
       // arh.tvAnimeInfo.text = String.format("%s - %s", animeList[pos].started_airing_date, animeList[pos].getUITypeText)

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