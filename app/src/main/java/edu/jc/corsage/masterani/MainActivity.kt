package edu.jc.corsage.masterani

import android.database.MatrixCursor
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.BaseColumns
import android.support.constraint.ConstraintLayout
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import edu.jc.corsage.masterani.Adapters.Menu.ExpandedMenuModel
import edu.jc.corsage.masterani.Adapters.Menu.SortMenuAdapter
import edu.jc.corsage.masterani.Adapters.SearchAdapter
import edu.jc.corsage.masterani.Fragments.*
import edu.jc.corsage.masterani.Masterani.Masterani
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    /* Fragments */
    private var homeFragment: HomeFragment? = null

    private var favoriteFragment: FavoriteFragment? = null
    private var sortFragment: SortFragment? = null
    private var scheduleFragment: ScheduleFragment? = null

    private var malFragment: MALFragment? = null
    private var kitsuFragment: KitsuFragment? = null

    private var settingFragment: SettingFragment? = null

    /* Sort Navigation Sub Menu */
    private var mDrawerLayout: DrawerLayout? = null
    private var mMenuAdapter: SortMenuAdapter? = null
    private var expandableList: ExpandableListView? = null

    private var listDataHeader: MutableList<ExpandedMenuModel> = ArrayList<ExpandedMenuModel>()
    private var listDataChild: HashMap<ExpandedMenuModel, List<String>> = HashMap<ExpandedMenuModel, List<String>>()

    /* Adapters */
    private var suggestionAdapter: SearchAdapter? = null

    /* Masterani */
    private var masterani: Masterani? = null

    init {
        homeFragment = HomeFragment()
    }

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
            //homeFragment = HomeFragment()

            // Pass intent extras to fragment.
            homeFragment?.arguments = intent.extras

            // Lastly, add the fragment to our view.
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, homeFragment).commit()
        }

        /* TEST: SORT SUB MENU ITEMS */
        //mDrawerLayout = findViewById(R.id.drawer_layout)
        //expandableList = findViewById(R.id.navigationmenu)

        //prepareSortSubMenu()
        //mMenuAdapter = SortMenuAdapter(this, listDataHeader, listDataChild, expandableList as ExpandableListView)

        //expandableList?.setAdapter(mMenuAdapter)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {

    }

    // Used for setting up Sort sub menu items.
    private fun prepareSortSubMenu() {
        val item1 = ExpandedMenuModel()
        item1.setIconName("heading1")
        item1.setIconImg(android.R.drawable.ic_delete)
        // Adding data header
        listDataHeader.add(item1)

        val item2 = ExpandedMenuModel()
        item2.setIconName("heading2")
        item2.setIconImg(android.R.drawable.ic_delete)
        listDataHeader.add(item2)

        val item3 = ExpandedMenuModel()
        item3.setIconName("heading3")
        item3.setIconImg(android.R.drawable.ic_delete)
        listDataHeader.add(item3)

        // Adding child data
        val heading1 = ArrayList<String>()
        heading1.add("Submenu of item 1")

        val heading2 = ArrayList<String>()
        heading2.add("Submenu of item 2")
        heading2.add("Submenu of item 2")
        heading2.add("Submenu of item 2")

        listDataChild.put(listDataHeader[0], heading1) // Header, Child data
        listDataChild.put(listDataHeader[1], heading2)
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

        if (suggestionAdapter == null) {
            val columns = arrayOf(BaseColumns._ID, "title", "info")
            val cursor = MatrixCursor(columns)
            suggestionAdapter = SearchAdapter(this, homeFragment as View.OnClickListener, cursor)
        }

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
        // TODO: Properly utilize addToBackStack.
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        when (item.itemId) {
            R.id.nav_favorite -> {
                if (favoriteFragment == null) {
                    favoriteFragment = FavoriteFragment()
                }
                ft.replace(R.id.fragment_container, favoriteFragment)
            }
            R.id.nav_home -> {
                if (homeFragment == null) {
                    homeFragment = HomeFragment()
                }
                ft.replace(R.id.fragment_container, homeFragment)
            }
            R.id.nav_sort -> {
                if (sortFragment == null) {
                    sortFragment = SortFragment()
                }
                ft.replace(R.id.fragment_container, sortFragment)
            }
            R.id.nav_schedule -> {
                if (scheduleFragment == null) {
                    scheduleFragment = ScheduleFragment()
                }
                ft.replace(R.id.fragment_container, scheduleFragment)
            }
            R.id.nav_mal -> {
                if (malFragment == null) {
                    malFragment = MALFragment()
                }
                ft.replace(R.id.fragment_container, malFragment)
            }
            R.id.nav_kitsu -> {
                if (kitsuFragment == null) {
                    kitsuFragment = KitsuFragment()
                }
                ft.replace(R.id.fragment_container, kitsuFragment)
            }
            R.id.nav_settings -> {
                if (settingFragment == null) {
                    settingFragment = SettingFragment()
                }
                ft.replace(R.id.fragment_container, settingFragment)
            }
        }

        ft.commit()
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
