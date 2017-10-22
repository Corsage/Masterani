package edu.jc.corsage.masterani.Fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import edu.jc.corsage.masterani.Adapters.ScheduleAdapter
import edu.jc.corsage.masterani.AnimeActivity
import edu.jc.corsage.masterani.R
import edu.jc.corsage.masterani.Sayonara.Entities.Schedule
import edu.jc.corsage.masterani.Sayonara.Sayonara

/**
 * Created by j3chowdh on 10/1/2017.
 */

class ScheduleFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var scheduleRefreshLayout: SwipeRefreshLayout? = null

    private var mondayRecyclerView: RecyclerView? = null
    private var tuesdayRecyclerView: RecyclerView? = null
    private var wednesdayRecyclerView: RecyclerView? = null
    private var thursdayRecyclerView: RecyclerView? = null
    private var fridayRecyclerView: RecyclerView? = null
    private var saturdayRecyclerView: RecyclerView? = null
    private var sundayRecyclerView: RecyclerView? = null

    private var mondayAdapter: ScheduleAdapter? = null
    private var tuesdayAdapter: ScheduleAdapter? = null
    private var wednesdayAdapter: ScheduleAdapter? = null
    private var thursdayAdapter: ScheduleAdapter? = null
    private var fridayAdapter: ScheduleAdapter? = null
    private var saturdayAdapter: ScheduleAdapter? = null
    private var sundayAdapter: ScheduleAdapter? = null

    private var sayonara: Sayonara? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if recreating instance.
        if (savedInstanceState != null) {
            mondayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_MONDAY"))
            tuesdayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_TUESDAY"))
            wednesdayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_WEDNESDAY"))
            thursdayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_THURSDAY"))
            fridayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_FRIDAY"))
            saturdayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_SATURDAY"))
            sundayAdapter = ScheduleAdapter(context, savedInstanceState.getParcelableArrayList("SCHEDULE_SUNDAY"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.view_schedule, container, false)

        scheduleRefreshLayout = view?.findViewById(R.id.scheduleRefreshLayout)

        mondayRecyclerView = view?.findViewById(R.id.lvMonday)
        tuesdayRecyclerView = view?.findViewById(R.id.lvTuesday)
        wednesdayRecyclerView = view?.findViewById(R.id.lvWednesday)
        thursdayRecyclerView = view?.findViewById(R.id.lvThursday)
        fridayRecyclerView = view?.findViewById(R.id.lvFriday)
        saturdayRecyclerView = view?.findViewById(R.id.lvSaturday)
        sundayRecyclerView = view?.findViewById(R.id.lvSunday)

        if (mondayAdapter == null ||
            tuesdayAdapter == null ||
            wednesdayAdapter == null ||
            thursdayAdapter == null ||
            fridayAdapter == null ||
            saturdayAdapter == null ||
            sundayAdapter == null  ) {
            ScheduleUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        } else {
            mondayRecyclerView?.adapter = mondayAdapter
            tuesdayRecyclerView?.adapter = tuesdayAdapter
            wednesdayRecyclerView?.adapter = wednesdayAdapter
            thursdayRecyclerView?.adapter = thursdayAdapter
            fridayRecyclerView?.adapter = fridayAdapter
            saturdayRecyclerView?.adapter = saturdayAdapter
            sundayRecyclerView?.adapter = sundayAdapter
        }

        // Fix for smooth scrolling.
        mondayRecyclerView?.isNestedScrollingEnabled = false
        tuesdayRecyclerView?.isNestedScrollingEnabled = false
        wednesdayRecyclerView?.isNestedScrollingEnabled = false
        thursdayRecyclerView?.isNestedScrollingEnabled = false
        fridayRecyclerView?.isNestedScrollingEnabled = false
        saturdayRecyclerView?.isNestedScrollingEnabled = false
        sundayRecyclerView?.isNestedScrollingEnabled = false

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scheduleRefreshLayout?.setOnRefreshListener(this)

        /*
        mondayRecyclerView?.onItemClickListener = this
        tuesdayRecyclerView?.onItemClickListener = this
        wednesdayRecyclerView?.onItemClickListener = this
        thursdayRecyclerView?.onItemClickListener = this
        fridayRecyclerView?.onItemClickListener = this
        saturdayRecyclerView?.onItemClickListener = this
        sundayRecyclerView?.onItemClickListener = this
        */
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList("SCHEDULE_MONDAY", mondayAdapter?.releases)
        outState?.putParcelableArrayList("SCHEDULE_TUESDAY", tuesdayAdapter?.releases)
        outState?.putParcelableArrayList("SCHEDULE_WEDNESDAY", wednesdayAdapter?.releases)
        outState?.putParcelableArrayList("SCHEDULE_THURSDAY", thursdayAdapter?.releases)
        outState?.putParcelableArrayList("SCHEDULE_FRIDAY", fridayAdapter?.releases)
        outState?.putParcelableArrayList("SCHEDULE_SATURDAY", saturdayAdapter?.releases)
        outState?.putParcelableArrayList("SCHEDULE_SUNDAY", sundayAdapter?.releases)
    }

    // SwipeRefreshLayout
    override fun onRefresh() {
        ScheduleUtil().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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

            // Need to sort by day.
            val tempMonday = ArrayList<Schedule>()
            val tempTuesday = ArrayList<Schedule>()
            val tempWednesday = ArrayList<Schedule>()
            val tempThursday = ArrayList<Schedule>()
            val tempFriday = ArrayList<Schedule>()
            val tempSaturday = ArrayList<Schedule>()
            val tempSunday = ArrayList<Schedule>()

            for (i in temp!!.indices) {
                when (temp[i].day_of_week) {
                    0 -> tempSunday.add(temp[i])
                    1 -> tempMonday.add(temp[i])
                    2 -> tempTuesday.add(temp[i])
                    3 -> tempWednesday.add(temp[i])
                    4 -> tempThursday.add(temp[i])
                    5 -> tempFriday.add(temp[i])
                    6 -> tempSaturday.add(temp[i])
                }
            }

            mondayAdapter = ScheduleAdapter(context, tempMonday)
            tuesdayAdapter = ScheduleAdapter(context, tempTuesday)
            wednesdayAdapter = ScheduleAdapter(context, tempWednesday)
            thursdayAdapter = ScheduleAdapter(context, tempThursday)
            fridayAdapter = ScheduleAdapter(context, tempFriday)
            saturdayAdapter = ScheduleAdapter(context, tempSaturday)
            sundayAdapter = ScheduleAdapter(context, tempSunday)
        }

        override fun onPostExecute(result: Unit?) {
            mondayRecyclerView?.adapter = mondayAdapter
            tuesdayRecyclerView?.adapter = tuesdayAdapter
            wednesdayRecyclerView?.adapter = wednesdayAdapter
            thursdayRecyclerView?.adapter = thursdayAdapter
            fridayRecyclerView?.adapter = fridayAdapter
            saturdayRecyclerView?.adapter = saturdayAdapter
            sundayRecyclerView?.adapter = sundayAdapter

            scheduleRefreshLayout?.isRefreshing = false
        }
    }
}