package edu.jc.corsage.masterani.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.Masterani.Entities.DetailedAnime
import edu.jc.corsage.masterani.R
import org.w3c.dom.Text

/**
 * Created by j3chowdh on 9/23/2017.
 */

class AnimeFragment : Fragment() {
    private lateinit var anime: DetailedAnime

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_anime, container, false)
        anime = arguments.getParcelable("ANIME")

        // Initialize all layout items.
        val wallpaper: ImageView = view.findViewById(R.id.wallpaper)
        val poster: ImageView = view.findViewById(R.id.poster)
        val title: TextView = view.findViewById(R.id.title)
        val type: TextView = view.findViewById(R.id.type)
        val status: TextView = view.findViewById(R.id.status)
        val ageRating: TextView = view.findViewById(R.id.ageRating)
        val synopsis: TextView = view.findViewById(R.id.synopsis)

        Glide.with(context)
                .load(anime.getRandomWallpaper)
                .apply(RequestOptions.centerCropTransform())
                .into(wallpaper)

        Glide.with(context)
                .load(anime.getPoster)
                .into(poster)

        title.text = anime.info.title
        type.text = anime.info.getTypeText
        status.text = anime.info.getStatusText
        ageRating.text = anime.info.age_rating
        synopsis.text = anime.info.synopsis

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Dynamically create Textviews for genres...
    }
}