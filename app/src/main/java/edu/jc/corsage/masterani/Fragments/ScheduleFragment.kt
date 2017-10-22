package edu.jc.corsage.masterani.Fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import edu.jc.corsage.masterani.Adapters.ScheduleAdapter
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Entities.Schedule
import edu.jc.corsage.masterani.Sayonara.Sayonara

/**
 * Created by j3chowdh on 10/1/2017.
 */

class ScheduleFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private var scheduleRefreshLayout: SwipeRefreshLayout? = null
    private var scheduleListView: ListView? = null
    private var scheduleAdapter: ScheduleAdapter? = null

    private var sayonara: Sayonara? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if recreating instance.
        if (savedInstanceState != null) {
            scheduleAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("schedule"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_schedule, container, false)

        scheduleRefreshLayout = view?.findViewById(R.id.scheduleRefreshLayout)
        scheduleListView = view?.findViewById(R.id.scheduleListView)

        if (scheduleAdapter == null) {
            ScheduleUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            scheduleListView?.adapter = scheduleAdapter
        }

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleRefreshLayout?.setOnRefreshListener(this)
        scheduleListView?.onItemClickListener = this
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("schedule", scheduleAdapter?.releases)
    }

    // SwipeRefreshLayout
    override fun onRefresh() {
        ScheduleUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    // onItemClick
    override fun onItemClick(p0: AdapterView<*>?, view: View?, p2: Int, p3: Long) {
        val id = view?.tag as Int
        val intent = Intent(activity, AnimeActivity::class.java)
        intent.putExtra("ID", id)
        activity?.startActivity(intent)
    }

    // ScheduleUtil AsyncTask
    inner class ScheduleUtil : AsyncTask<Int?, Unit, Unit>() {
        override fun onPreExecute() {
            scheduleRefreshLayout?.isRefreshing = true
        }

        override fun doInBackground(vararg p0: Int?) {
            if (sayonara == null) {
                sayonara = Sayonara()
            }

            val temp = sayonara?.getSchedule()
            scheduleAdapter = ScheduleAdapter(context, temp as ArrayList<Schedule>)
        }

        override fun onPostExecute(result: Unit?) {
            scheduleListView?.adapter = scheduleAdapter
            scheduleRefreshLayout?.isRefreshing = false
        }
    }
}