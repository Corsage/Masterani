package edu.jc.corsage.masterani.Fragments

import android.os.AsyncTask
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
import edu.jc.corsage.masterani.Masterani.Masterani
import edu.jc.corsage.masterani.R
import me.chensir.expandabletextview.ExpandableTextView

/**
 * Created by j3chowdh on 9/23/2017.
 */

class AnimeFragment : Fragment() {
    private var anime: DetailedAnime? = null

    private var wallpaper: ImageView? = null
    private var poster: ImageView? = null
    private var title: TextView? = null
    private var type: TextView? = null
    private var tvStatus: TextView? = null
    private var ageRating: TextView? = null
    private var synopsis: ExpandableTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            anime = savedInstanceState.getParcelable("ANIME")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_anime, container, false)

        // Initialize all layout items.
        wallpaper = view.findViewById(R.id.wallpaper)
        poster = view.findViewById(R.id.poster)
        title = view.findViewById(R.id.title)
        type = view.findViewById(R.id.type)
        tvStatus = view.findViewById(R.id.status)
        ageRating = view.findViewById(R.id.ageRating)
        synopsis = view.findViewById(R.id.synopsis)

        if (anime != null) {
            title?.text = anime?.info?.title
            type?.text = anime?.info?.getTypeText
            tvStatus?.text = anime?.info?.getStatusText
            ageRating?.text = anime?.info?.age_rating
            synopsis?.text = anime?.info?.synopsis

            Glide.with(context)
                    .load(anime?.getRandomWallpaper)
                    .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                    .apply(RequestOptions.centerCropTransform())
                    .into(wallpaper)

            Glide.with(context)
                    .load(anime?.getPoster)
                    .apply(RequestOptions.placeholderOf(R.drawable.default_poster))
                    .into(poster)
        } else {
            AnimeUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arguments.getInt("ID"))
        }
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Dynamically create Textviews for genres...
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if (outState != null) {
            if (!outState.containsKey("ANIME")) {
                outState.putParcelable("ANIME", anime)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("AnimeFragment", "Destroying...")

        anime = null
        wallpaper = null
        poster = null
        title = null
        type = null
        tvStatus = null
        ageRating = null
        synopsis = null
    }

     inner class AnimeUtil : AsyncTask<Int?, Unit, Unit>() {
         override fun doInBackground(vararg p0: Int?) {
             anime = Masterani().getSpecificAnime(p0[0])
         }

         override fun onPostExecute(result: Unit?) {
             Glide.with(context)
                     .load(anime?.getRandomWallpaper)
                     .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                     .apply(RequestOptions.centerCropTransform())
                     .into(wallpaper)

             Glide.with(context)
                     .load(anime?.getPoster)
                     .apply(RequestOptions.placeholderOf(R.drawable.default_poster))
                     .into(poster)

             title?.text = anime?.info?.title
             type?.text = anime?.info?.getTypeText
             tvStatus?.text = anime?.info?.getStatusText
             ageRating?.text = anime?.info?.age_rating
             synopsis?.text = anime?.info?.synopsis
         }
    }
}