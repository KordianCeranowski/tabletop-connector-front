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
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.EventRepository
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.shortToast
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logD
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.io.Serializable

@UnreliableToastApi
class MainActivity : BaseActivity() {

    override val binding: ActivityMainBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    private lateinit var toggle: ActionBarDrawerToggle

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
    }

    private fun setupSidebar() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout,
            R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
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

        val eventsMock = List(10) { idx ->
            Event("Event ${idx + 1}",
                getMockUser(),
                getRandomDate(),
                getMockAddress(),
                emptyList(),
                List(10) {
                    Game(
                        "Name ${it + 1}",
                        "URI",
                        2,
                        (3..8).random(),
                        (15..90 step 5).random()
                    )
                }
            )
        }

        setFragmentAndTitle(ListOfEventsFragment(), "Dashboard")

        binding.nvSidebar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_profile -> setFragmentAndTitle(ProfileFragment(),"Profile")
                R.id.mi_all_events -> setFragmentAndTitle(ListOfEventsFragment(), "Dashboard")
                R.id.mi_my_events -> shortToast("Clicked My Events")
                R.id.mi_events_history -> shortToast("Clicked Events History")
                R.id.mi_settings -> setFragmentAndTitle(SettingsFragment(), "Settings")
                R.id.mi_about -> setFragmentAndTitle(AboutFragment(), "About")
                R.id.mi_logout -> logout()
            }
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
            settingsManager.run {
                setIsUserLoggedIn(false)
                setUserAccessToken("")
                setUserRefreshToken("")
            }
        }
        start<LoginActivity>()
        finish()
    }
}