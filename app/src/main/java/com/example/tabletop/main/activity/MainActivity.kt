package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.io.Serializable

@UnreliableToastApi
class MainActivity : BaseActivity() {

    override val binding: ActivityMainBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    private lateinit var toggle: ActionBarDrawerToggle

    private var isMenuItemFilterVisible = true

    // Setup
    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
    }

    private fun setupSidebar() {
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Setup welcome message
        val userFirstName = runBlocking { settingsManager.userFirstNameFlow.first() }
        val tvUserFirstName = binding.nvSidebar.getHeaderView(0).tv_nav_header_user_firstname
        tvUserFirstName.text = "Welcome, $userFirstName"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        setupSidebar()

        attachObserverLogout()

        val userId = runBlocking { settingsManager.userIdFlow.first() }

        val isMyEvents = intent.getBooleanExtra(Extra.IS_MY_EVENTS(), false)

        val queryMap = if (isMyEvents) mapOf(Query.PARTICIPANT to userId) else emptyMap()

        val bundleQueryMap = Bundle().apply {
            putSerializable(Extra.QUERY_MAP(), queryMap as Serializable)
        }

        setupInitialFragment(isMyEvents, bundleQueryMap)

        setupSidebarItemSelectedListener(bundleQueryMap)
    }

    // Sidebar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.mi_main_filter).isVisible = isMenuItemFilterVisible

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        } else {
            when (item.itemId) {
                R.id.mi_main_filter -> showAlertDialogFilter()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSidebarItemSelectedListener(bundleMyEvents: Bundle) {
        binding.nvSidebar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_profile ->
                    start<ProfileActivity>()
                R.id.mi_all_events ->
                    setFragmentAndTitle(ListOfEventsFragment(), "Events")
                R.id.mi_my_events ->
                    setFragmentAndTitle(
                        ListOfEventsFragment().apply { arguments = bundleMyEvents },
                        "My Events"
                    )
                R.id.mi_account ->
                    start<AccountActivity>()
                R.id.mi_about ->
                    setFragmentAndTitle(AboutFragment(), "About")
                R.id.mi_logout ->
                    logout()
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    // Alert Dialog Filter
    private fun showAlertDialogFilter() {
        val (longitude, latitude) = getCurrentLocation()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter")

        val customLayout = layoutInflater.inflate(R.layout.dialog_box_filter, null)
        builder.setView(customLayout)

        val sbDistance = customLayout.findViewById<SeekBar>(R.id.sb_distance)

        val tvDistanceCounter = customLayout.findViewById<TextView>(R.id.tv_distance_counter)

        sbDistance.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    tvDistanceCounter.text = "${p0?.progress} km"
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            }
        )

        builder.setPositiveButton("OK") { _, _ ->
            val queryMap = mapOf(
                Query.DISTANCE to sbDistance.progress.toString(),
                //Query.DATE_FROM to
                //Query.DATE_TO to
                Query.GEO_X to longitude.toString(),
                Query.GEO_Y to latitude.toString()
            )
            sendDialogDataToActivity(queryMap)
        }

        builder.setNegativeButton("Cancel") { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }

    private fun sendDialogDataToActivity(queryMap: Map<Query, String>) {
        logD(queryMap.toString())
        val bundle = Bundle().apply {
            putSerializable(Extra.QUERY_MAP(), queryMap as Serializable)
        }
        setFragmentAndTitle(ListOfEventsFragment().apply { arguments = bundle }, "Events")
    }

    // Fragments
    private fun setupInitialFragment(isMyEvents: Boolean, bundleQueryMap: Bundle) {
        setFragmentAndTitle(
            ListOfEventsFragment().apply { arguments = bundleQueryMap },
            if (isMyEvents) "My Events" else "Events"
        )
    }

    private fun setFragmentAndTitle(fragment: Fragment, title: String) {
        setCurrentFragment(fragment)
        setActionBarTitle(title)
    }

    private fun setCurrentFragment(fragment: Fragment) {
        isMenuItemFilterVisible = (fragment.className == "ListOfEventsFragment").also {
            invalidateOptionsMenu()
        }

        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragmentMain.id, fragment)
            commit()
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    // Logout
    private fun logout() {
        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        userViewModel.logout(accessToken)
        runBlocking {
            settingsManager.run {
                setUserAccessToken("")
                setUserFirstName("")
                setUserId("")
            }
        }
        start<LoginActivity>()
        finish()
    }

    private fun attachObserverLogout() {
        userViewModel.responseLogout.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Unit>) {
        val onSuccess = {
            runBlocking {
                settingsManager.run {
                    setUserAccessToken("")
                    setUserId("")
                }
            }
            logI("Logged out successfully")
            toast("Logged out")
        }

        val onFailure = {
            logW(response.getFullResponse())
            logW(response.getErrorBodyProperties().toString())
            toast("Something went wrong with logging out")
        }

        response.resolve(onSuccess, onFailure)
    }
}