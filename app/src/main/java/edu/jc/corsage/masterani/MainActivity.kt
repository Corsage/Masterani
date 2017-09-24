package edu.jc.corsage.masterani

import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import edu.jc.corsage.masterani.Adapters.SearchAdapter
import edu.jc.corsage.masterani.Fragments.HomeFragment
import edu.jc.corsage.masterani.Fragments.SortFragment
import edu.jc.corsage.masterani.Masterani.Masterani
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private var homeFragment: HomeFragment? = null
    private var sortFragment: SortFragment? = null
    private var masterani: Masterani? = null
    private var suggestionAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // Instantiate Masterani class.
        masterani = Masterani()

        // Checking if home isn't defaulted.
        if (findViewById<ConstraintLayout>(R.id.fragment_container) != null) {
            // Check if restoring from previous state.
            // If true, return as we should not overlap fragments.
            if (savedInstanceState != null) {
                return
            }

            // Create the home fragment.
            homeFragment = HomeFragment()

            // Pass intent extras to fragment.
            homeFragment?.arguments = intent.extras

            // Lastly, add the fragment to our view.
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, homeFragment).commit()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        // Minimum auto-search...
        if (newText.length >= 3) {
            masterani?.getSearchAnimeResults(suggestionAdapter, newText)
        }

        Log.d("search", "searching for " + newText)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        // Allow user to search any length if they decide to submit it.
        masterani?.getSearchAnimeResults(suggestionAdapter, query)

        Log.d("search", "submitted: " + query)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        Log.d("onCreateOptionsMenu", "id for search is : " + R.id.animeSearch.toString())

        val columns = arrayOf(BaseColumns._ID,
                "title", "info")

        val cursor = MatrixCursor(columns)

        suggestionAdapter = SearchAdapter(this, homeFragment as View.OnClickListener, cursor)

        // Get SearchView and setup the searchable configuration.
        val searchView: SearchView = menu.findItem(R.id.animeSearch).actionView as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.suggestionsAdapter = suggestionAdapter

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
           // R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_favorite -> {

            }
            R.id.nav_sort -> {
                if (sortFragment == null) {
                    sortFragment = SortFragment()
                }
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, sortFragment)
                        .addToBackStack(null)
                        .commit()
            }
            R.id.nav_schedule -> {

            }
            R.id.nav_mal -> {

            }
            R.id.nav_kitsu -> {

            }
            R.id.nav_settings -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
