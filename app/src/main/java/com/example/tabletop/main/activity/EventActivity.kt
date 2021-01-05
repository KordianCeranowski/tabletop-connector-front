package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.viewmodel.BottomNavBarViewModel
import com.example.tabletop.mvvm.viewmodel.EventViewModel
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
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.io.Serializable


@Suppress("COMPATIBILITY_WARNING")
@UnreliableToastApi
class EventActivity : BaseActivity() {

    override val binding: ActivityEventBinding by viewBinding()

    private val eventViewModel by lazyViewModels { EventViewModel() }

    private val bottomNavBarViewModel by lazyViewModels { BottomNavBarViewModel() }

    private lateinit var settingsManager: SettingsManager

    private lateinit var currentEvent: Event

    private val accessToken: String
        get() = runBlocking { settingsManager.userAccessTokenFlow.first() }

    private val userId: String
        get() = runBlocking { settingsManager.userIdFlow.first() }

    private lateinit var selectedFragment: Fragment

    // Setup
    override fun setup() {
        binding
        setActionBarTitle("Event")

        settingsManager = SettingsManager(applicationContext)
        currentEvent = intent.getSerializableExtra(Extra.EVENT()) as Event
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserverResponseOne()

        selectFragment(EventInfoFragment())

        setupBottomNavBarItemSelectedIListener()

        // todo (maybe?) Chat notifications
        /*bottomNavigationView.getOrCreateBadge(R.id.mi_chat).apply {
            eventChatFragment?.let { number = 3 }
        }*/
    }

    // Top Right Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (userId == currentEvent.creator.id) {
            menuInflater.inflate(R.menu.event_info_menu, menu)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_event_edit -> startWithExtra<EventEditActivity>(
                Extra.EVENT_ID() to currentEvent.id
            )
        }
        return true
    }

    private fun setupBottomNavBarItemSelectedIListener() {
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mi_info -> selectFragment(EventInfoFragment())
                R.id.mi_games -> selectFragment(EventGamesFragment())
                R.id.mi_location -> selectFragment(EventLocationFragment())
                R.id.mi_participants -> selectFragment(EventParticipantsFragment())
                R.id.mi_chat -> selectFragment(EventChatFragment()) //currentEvent.chat?.let {}
            }
            true
        }

        val miChat =
            binding.bottomNavBar.menu.children.find { it.itemId == R.id.mi_chat } as MenuItem

        bottomNavBarViewModel.isChatEnabled.observe(this) { isChatEnabled ->
            if (isChatEnabled) {
                miChat.isEnabled = true.also { logV("Chat enabled") }
            } else {
                miChat.isEnabled = false.also { logV("Chat disabled") }
            }
        }
        bottomNavBarViewModel.setChatEnabled(true)
    }

    private fun selectFragment(fragment: Fragment) {
        val fragments = supportFragmentManager.fragments
        val currentFragment = if (fragments.isEmpty()) Fragment() else fragments.last()
        if (fragment.className != currentFragment.className) {
            selectedFragment = fragment
            retrieveEvent(accessToken, currentEvent.id)
        }
    }

    // Retrieve Event
    private fun retrieveEvent(accessToken: String, eventId: String) {
        eventViewModel.getOne(accessToken, eventId)
    }

    private fun attachObserverResponseOne() {
        eventViewModel.responseOne.observe(this) {
            handleResponseRetrieveEvent(it)
        }
    }

    private fun handleResponseRetrieveEvent(response: Response<Event>) {
        val onSuccess = {
            logD(response.status())
            currentEvent = response.body()!!
            replaceFragmentWithSelected()
        }

        val onFailure = {
            toast("Something went wrong while retrieving event")
            logW(response.getFullResponse())
            logI(response.getErrorJson().toString())
        }

        response.resolve(onSuccess, onFailure)
    }

    private fun replaceFragmentWithSelected() {
        val bundle = Bundle().apply {
            putSerializable(Extra.EVENT(), currentEvent as Serializable)
        }

        val fragmentWithBundle = selectedFragment.apply { arguments = bundle }

        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragmentEvent.id, fragmentWithBundle)
            commit()
        }
    }
}