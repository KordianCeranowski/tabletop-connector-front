package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class MainActivity : BaseActivity() {

    override val binding: ActivityMainBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    private lateinit var toggle: ActionBarDrawerToggle

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        logI("Starting ${this.className}")
    }

    private fun setupSidebar() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout,
            R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        setupSidebar()

        attachObserverLogout()

        val bundleAllEvents = Bundle().apply { putBoolean("IS_ALL_EVENTS", true) }
        val bundleMyEvents = Bundle().apply { putBoolean("IS_ALL_EVENTS", false) }

        val isShowMyEvents = intent.getBooleanExtra("IS_SHOW_MY_EVENTS", false)
        if (isShowMyEvents) {
            setFragmentAndTitle(
                ListOfEventsFragment().apply { arguments = bundleMyEvents },
                "My Events"
            )
        } else {
            setFragmentAndTitle(
                ListOfEventsFragment().apply { arguments = bundleAllEvents },
                "Events"
            )
        }


        binding.nvSidebar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_profile -> start<ProfileActivity>()
                R.id.mi_all_events -> setFragmentAndTitle(
                    ListOfEventsFragment().apply { arguments = bundleAllEvents },
                    "Events"
                )
                R.id.mi_my_events -> setFragmentAndTitle(
                    ListOfEventsFragment().apply { arguments = bundleMyEvents },
                    "My Events"
                )
                R.id.mi_settings -> setFragmentAndTitle(SettingsFragment(), "Settings")
                R.id.mi_about -> setFragmentAndTitle(AboutFragment(), "About")
                R.id.mi_logout -> logout()
            }
            binding.drawerLayout.closeDrawers()
            true
        }
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

    private fun logout() {
        lifecycleScope.launch {
            val accessToken = settingsManager.userAccessTokenFlow.first()
            UserViewModel.logout(accessToken)
            start<LoginActivity>()
            finish()
        }
    }

    private fun attachObserverLogout() {
        UserViewModel.responseLogout.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Unit>) {
        val onSuccess = {
            lifecycleScope.launch {
                withContext(Dispatchers.Default) {
                    settingsManager.run {
                        setUserAccessToken("")
                        setUserId("")
                    }
                }
                logI("Logged out successfully")
                toast("Logged out")
            }
        }

        val onFailure = {
            logW(response.getFullResponse())
            logW(response.getErrorBodyProperties().toString())
            toast("Something went wrong with logging out")
        }

        response.resolve(onSuccess, onFailure)
    }
}