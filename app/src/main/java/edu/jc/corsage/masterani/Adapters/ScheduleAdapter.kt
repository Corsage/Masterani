package edu.jc.corsage.masterani.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Entities.Schedule

/**
 * Created by j3chowdh on 10/21/2017.
 */

class ScheduleAdapter(val context: Context, val releases: ArrayList<Schedule>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return releases.size
    }

    override fun getItem(pos: Int): Any {
        return releases[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(pos: Int, view: View?, viewGroup: ViewGroup?): View {
        val v: View

        if (view == null) {
            v = this.inflater.inflate(R.layout.item_schedule, viewGroup, false)
        } else {
            v = view
        }

        v.tag = releases[pos].anime.id

        val srh = ScheduleRowHolder(v)

        Glide.with(context)
                .load(releases[pos].anime.poster.getPoster)
                .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                .apply(RequestOptions.centerCropTransform())
                .into(srh.schedulePoster)

        srh.scheduleInfo.text = releases[pos].getReleaseTime + " - EP " + releases[pos].next_episode
        srh.scheduleAnimeTitle.text = releases[pos].anime.title

        return v
    }

    inner class ScheduleRowHolder(view: View) {
        val schedulePoster: ImageView = view.findViewById(R.id.schedulePoster)
        val scheduleInfo: TextView = view.findViewById(R.id.scheduleInfo)
        val scheduleAnimeTitle: TextView = view.findViewById(R.id.scheduleAnimeTitle)
    }
}