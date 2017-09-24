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
import edu.jc.corsage.masterani.Masterani.Entities.DetailedEpisode
import edu.jc.corsage.masterani.R
import org.w3c.dom.Text

/**
 * Created by j3chowdh on 9/23/2017.
 */

class EpisodeAdapter(val context: Context, val episodeList: List<DetailedEpisode>) : BaseAdapter() {
    private val inflator = LayoutInflater.from(context)

    override fun getCount(): Int {
        return episodeList.size
    }

    override fun getItem(pos: Int): Any {
        return episodeList[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(pos: Int, view: View?, viewGroup: ViewGroup?): View {
        val v: View

        if (view == null) {
            v = this.inflator.inflate(R.layout.item_episode, viewGroup, false)
        } else {
            v = view
        }

        v.tag = episodeList[pos].info

        val erh = EpisodeRowHolder(v)

        Glide.with(context)
                .load(episodeList[pos].getThumbnail)
                .apply(RequestOptions.centerCropTransform())
                .into(erh.episodePoster)

        erh.duration.text = episodeList[pos].info?.getDurationText
        erh.episodeName.text = episodeList[pos].info?.title
        erh.episodeNumber.text = episodeList[pos].info?.getEpisodeText

        return v
    }

    inner class EpisodeRowHolder(view: View) {
        val episodePoster: ImageView = view.findViewById(R.id.episodePoster)
        val duration: TextView = view.findViewById(R.id.duration)
        val episodeName: TextView = view.findViewById(R.id.episodeName)
        val episodeNumber: TextView = view.findViewById(R.id.episodeNumber)
    }
}