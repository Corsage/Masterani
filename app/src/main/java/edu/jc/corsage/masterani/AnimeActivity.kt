package edu.jc.corsage.masterani

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import edu.jc.corsage.masterani.Fragments.AnimeFragment
import edu.jc.corsage.masterani.Fragments.EpisodeFragment
import kotlinx.android.synthetic.main.activity_anime.*

class AnimeActivity : AppCompatActivity() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var ID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve anime ID from intent.
        ID = intent.extras.getInt("ID")

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        // Set up the TabLayout using the ViewPager.
        animeTabs.setupWithViewPager(container)
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            // getItem is called to instantiate the fragment for the given page.
            var fragment: Fragment? = null
            val args = Bundle()
            args.putInt("ID", ID as Int)

            when (position) {
                0 -> {
                    fragment = AnimeFragment()
                    fragment.arguments = args
                    return fragment
                }
                1 -> {
                    fragment = EpisodeFragment()
                    fragment.arguments = args
                    return fragment
                }
                else -> return fragment
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Info"
                1 -> return "Episodes"
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 2 total pages.
            return 2
        }
    }
}
