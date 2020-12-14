package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.databinding.ActivityMainBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.repository.EventRepository
import com.example.tabletop.util.Helpers.getMockAddress
import com.example.tabletop.util.Helpers.getRandomDate
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.util.Helpers.getFullResponse
import com.example.tabletop.util.random
import dev.ajkueterman.lazyviewmodels.lazyActivityViewModels
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_main.*
import net.alexandroid.utils.mylogkt.logD
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.io.Serializable
import java.util.*

@UnreliableToastApi
class MainActivity : BaseActivity() {

    override val binding: ActivityMainBinding by viewBinding()

    private lateinit var toggle: ActionBarDrawerToggle

    private val eventViewModel: EventViewModel by lazyViewModels {
        EventViewModel(EventRepository)
    }

    override fun setup() {

    }

    private fun setupSidebar() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout,
            R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //setSupportActionBar(binding.toolbar)

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
                "Creator ${idx + 1}",
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

        // eventViewModel.getMany("id","desc")
        //
        // lateinit var events: List<Event>
        // eventViewModel.responseMany.observe(this) { response ->
        //     if (response.isSuccessful) {
        //         logD(response.getFullResponse())
        //         response.body()?.let { events = it.results }
        //     } else {
        //         toast(response.code())
        //     }
        // }

        val bundle = Bundle().apply { putSerializable("EVENTS", eventsMock as Serializable) }

        setCurrentFragment(ListOfEventsFragment().apply { arguments = bundle })

        binding.nvSidebar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_profile -> setCurrentFragment(ProfileFragment())
                R.id.mi_all_events -> setCurrentFragment(
                    ListOfEventsFragment().apply { arguments = bundle }
                )
                R.id.mi_my_events -> toast("Clicked My Events")
                R.id.mi_events_history -> toast("Clicked Events History")
                R.id.mi_settings -> setCurrentFragment(SettingsFragment())
                R.id.mi_about -> setCurrentFragment(AboutFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragmentMain.id, fragment)
            commit()
        }
    }
}