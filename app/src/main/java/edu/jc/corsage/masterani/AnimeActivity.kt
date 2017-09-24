package edu.jc.corsage.masterani

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import edu.jc.corsage.masterani.Fragments.AnimeFragment
import edu.jc.corsage.masterani.Fragments.EpisodeFragment
import edu.jc.corsage.masterani.Masterani.Entities.DetailedAnime
import edu.jc.corsage.masterani.Masterani.Entities.DetailedEpisode
import kotlinx.android.synthetic.main.activity_anime.*
import java.util.ArrayList

// TODO: Retrieve anime object here instead from intent. Due to large episodes sometimes...

class AnimeActivity : AppCompatActivity() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    private var anime: DetailedAnime? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Retrieve anime object from intent.
        anime = intent.extras.getParcelable("ANIME")

        // Create the adapter that will return a fragment for each of the three
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

            when (position) {
                0 -> {
                    val args = Bundle()
                    args.putParcelable("ANIME", anime)
                    fragment = AnimeFragment()
                    fragment.arguments = args
                    return fragment
                }
                1 -> {
                    val args = Bundle()
                    args.putParcelableArrayList("EPISODES", anime?.episodes as ArrayList<DetailedEpisode>)
                    args.putString("SLUG", anime?.info?.slug)
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
