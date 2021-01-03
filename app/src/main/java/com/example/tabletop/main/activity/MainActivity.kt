package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import android.widget.EditText
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logV
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

    // Setup
    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        logI("Starting ${this.className}")
    }

    private fun setupSidebar() {
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                R.id.mi_settings ->
                    setFragmentAndTitle(SettingsFragment(), "Settings")
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
        val userLocation = object {
            val longitude = runBlocking { settingsManager.userLongitudeFlow.first() }
            val latitude = runBlocking { settingsManager.userLatitudeFlow.first() }
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter")

        val customLayout = layoutInflater.inflate(R.layout.dialog_box_filter, null)
        builder.setView(customLayout)

        builder.setPositiveButton("OK") { _, _ ->
            val dialogBox = object {
                val distance = customLayout.findViewById<EditText>(R.id.ti_et_dialog_box_distance)
                    .text.toString()
                val name = customLayout.findViewById<EditText>(R.id.ti_et_dialog_box_name)
                    .text.toString()
            }
            val queryMap = mapOf(
                Query.DISTANCE to dialogBox.distance,
                Query.NAME to dialogBox.name,
                Query.GEO_X to userLocation.longitude.toString(),
                Query.GEO_Y to userLocation.latitude.toString()
            )
            sendDialogDataToActivity(queryMap)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            //dialog.cancel()
            //dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun sendDialogDataToActivity(queryMap: Map<Query, String>) {
        logD(queryMap.toString())
        val bundle = Bundle().apply {
            putSerializable(Extra.QUERY_MAP(), queryMap as Serializable)
        }
        setFragmentAndTitle(ListOfEventsFragment().apply { arguments = bundle }, "My Events")
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