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
import java.util.*

/**
 * Created by j3chowdh on 9/23/2017.
 */

class AnimeFragment : Fragment() {
    private lateinit var anime: DetailedAnime

    private lateinit var wallpaper: ImageView
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var type: TextView
    private lateinit var tvStatus: TextView
    private lateinit var ageRating: TextView
    private lateinit var synopsis: ExpandableTextView

    // Hold wallpapers and poster for re-creating layout.
    private var posterURL: String? = null
    private var wallpaperURLs: ArrayList<String>? = null

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

        if (savedInstanceState != null) {
            title.text = savedInstanceState.getString("TITLE")
            type.text = savedInstanceState.getString("TYPE")
            tvStatus.text = savedInstanceState.getString("STATUS")
            ageRating.text = savedInstanceState.getString("AGE_RATING")
            synopsis.text = savedInstanceState.getString("SYNOPSIS")
            posterURL = savedInstanceState.getString("POSTER")
            wallpaperURLs = savedInstanceState.getStringArrayList("WALLPAPERS")

            if (wallpaperURLs != null && wallpaperURLs!!.isNotEmpty()) {
                Glide.with(context)
                        .load(wallpaperURLs!!.get(Random().nextInt(wallpaperURLs!!.size)))
                        .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                        .apply(RequestOptions.centerCropTransform())
                        .into(wallpaper)
            } else {
                Glide.with(context)
                        .load(null)
                        .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                        .apply(RequestOptions.centerCropTransform())
                        .into(wallpaper)
            }

            Glide.with(context)
                    .load(posterURL)
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

        outState?.putString("TITLE", title.text as String)
        outState?.putString("TYPE", type.text as String)
        outState?.putString("STATUS", tvStatus.text as String)
        outState?.putString("AGE_RATING", ageRating.text as String)
        outState?.putString("SYNOPSIS", synopsis.text as String)
        outState?.putStringArrayList("WALLPAPERS", wallpaperURLs)
        outState?.putString("POSTER", posterURL)
    }

     inner class AnimeUtil : AsyncTask<Int?, Unit, Unit>() {
         override fun doInBackground(vararg p0: Int?) {
             Log.d("AnimeUtil","starting with " + p0[0])
             anime = Masterani().getSpecificAnime(p0[0])

             // Give to local variables.
             posterURL = anime.getPoster

             if (anime.wallpapers != null && anime.wallpapers!!.isNotEmpty()) {
                 wallpaperURLs = ArrayList()
                 for (i in anime.wallpapers!!.indices) {
                     wallpaperURLs?.add(Masterani.CDN.WALLPAPER_URL + anime.wallpapers!!.get(i).file)
                 }
             }
         }

         override fun onPostExecute(result: Unit?) {
             Glide.with(context)
                     .load(anime.getRandomWallpaper)
                     .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                     .apply(RequestOptions.centerCropTransform())
                     .into(wallpaper)

             Glide.with(context)
                     .load(anime.getPoster)
                     .into(poster)

             title.text = anime.info.title
             type.text = anime.info.getTypeText
             tvStatus.text = anime.info.getStatusText
             ageRating.text = anime.info.age_rating
             synopsis.text = anime.info.synopsis
         }
    }
}