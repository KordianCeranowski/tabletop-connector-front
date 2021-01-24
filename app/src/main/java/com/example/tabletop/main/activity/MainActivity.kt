package com.example.tabletop.main.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.main.fragment.AboutFragment
import com.example.tabletop.main.fragment.ListOfEventsFragment
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
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat", "SetTextI18n")
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

        val bundleQueryMapParticipant = Bundle().apply {
            putSerializable(Extra.QUERY_MAP(), mapOf(Query.PARTICIPANT to userId) as Serializable)
        }
        setupSidebarItemSelectedListener(bundleQueryMapParticipant)
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

    private fun setupSidebarItemSelectedListener(bundleQueryMapParticipant: Bundle) {
        binding.nvSidebar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_profile ->
                    start<ProfileActivity>()
                R.id.mi_all_events ->
                    setFragmentAndTitle(ListOfEventsFragment(), "Events")
                R.id.mi_my_events ->
                    setFragmentAndTitle(
                        ListOfEventsFragment().apply { arguments = bundleQueryMapParticipant },
                        "My Events"
                    )
                R.id.mi_account ->
                    start<AccountActivity>()
                R.id.mi_logout ->
                    logout()
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    // Alert Dialog Filter
    private fun showAlertDialogFilter() {
        //TODO
        //val (longitude, latitude) = getCurrentLocation()
        val (longitude, latitude) = (54.395704550000005 to 18.5739726651911)

        runBlocking {
            settingsManager.run {
                setLongitude(longitude)
                setLatitude(latitude)
            }
        }

        val layout = layoutInflater.inflate(R.layout.alert_dialog_event_filter, null)

        val sbDistance = layout.findViewById<SeekBar>(R.id.sb_distance)
        val tvDistanceCounter = layout.findViewById<TextView>(R.id.tv_distance_counter)

        sbDistance.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    tvDistanceCounter.text = "${p0?.progress} km"
                }
                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            }
        )

        val llDateFrom = layout.findViewById<LinearLayout>(R.id.linear_layout_filter_date_from)
        val llDateTo = layout.findViewById<LinearLayout>(R.id.linear_layout_filter_date_to)

        val tvDateFrom =
            llDateFrom.findViewById<TextView>(R.id.tv_filter_date_from)
                .apply { text = getCurrentDate() }

        val tvDateTo =
            llDateTo.findViewById<TextView>(R.id.tv_filter_date_to)
                .apply { text = getCurrentDate() }

        llDateFrom.setOnClickListener { handleDateClick(tvDateFrom, "From") }
        llDateTo.setOnClickListener { handleDateClick(tvDateTo, "To") }

        val builder = AlertDialog.Builder(this).apply {
            setTitle("Filter")
            setView(layout)

            setPositiveButton("OK") { _, _ ->
                val queryMap = mapOf(
                    Query.DISTANCE to sbDistance.progress.toString(),
                    Query.DATE_FROM to tvDateFrom.text.toString(),
                    Query.DATE_TO to tvDateTo.text.toString(),
                    Query.GEO_X to longitude.toString(),
                    Query.GEO_Y to latitude.toString()
                )
                sendDialogDataToActivity(queryMap)
            }

            setNegativeButton("Cancel") { _, _ -> }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun handleDateClick(textView: TextView, prefix: String) {
        val calendar = Calendar.getInstance()

        val initialDate = object {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

            textView.text = "$prefix $date"
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener, initialDate.year, initialDate.month, initialDate.day
        )
        datePickerDialog.show()
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
            val errorJson = response.getErrorJson()

            logW(response.getFullResponse())
            logW(errorJson.toString())

            toast("Something went wrong")
        }

        response.resolve(onSuccess, onFailure)
    }
}