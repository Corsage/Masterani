package edu.jc.corsage.masterani.Adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Entities.Schedule

/**
 * Created by j3chowdh on 10/21/2017.
 */

class ScheduleAdapter(val context: Context, val releases: ArrayList<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleRowHolder>(), View.OnClickListener {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int {
        return releases.size
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun onClick(view: View) {
        val id = view.tag as Int
        val intent = Intent(context, AnimeActivity::class.java)
        intent.putExtra("ID", id)
        context.startActivity(intent)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): ScheduleRowHolder {
        val view = inflater.inflate(R.layout.item_schedule, viewGroup, false)
        return ScheduleRowHolder(view)
    }

    override fun onBindViewHolder(srh: ScheduleRowHolder, pos: Int) {
        srh.view.tag = releases[pos].anime.id

        Glide.with(context)
                .load(releases[pos].anime.poster.getPoster)
                .apply(RequestOptions.placeholderOf(R.drawable.default_wallpaper))
                .apply(RequestOptions.centerCropTransform())
                .into(srh.schedulePoster)

        srh.scheduleInfo.text = releases[pos].getReleaseTime + " - EP " + releases[pos].next_episode
        srh.scheduleAnimeTitle.text = releases[pos].anime.title

        srh.view.setOnClickListener(this)
    }

    inner class ScheduleRowHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val schedulePoster: ImageView = view.findViewById(R.id.schedulePoster)
        val scheduleInfo: TextView = view.findViewById(R.id.scheduleInfo)
        val scheduleAnimeTitle: TextView = view.findViewById(R.id.scheduleAnimeTitle)
    }
}