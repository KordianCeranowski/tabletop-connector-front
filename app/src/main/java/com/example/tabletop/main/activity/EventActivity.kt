package com.example.tabletop.main.activity

import android.os.Bundle
import android.text.Layout
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.fragment.app.Fragment
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.util.EXTRA_EVENT
import com.example.tabletop.util.className
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import splitties.toast.UnreliableToastApi
import java.io.Serializable

@UnreliableToastApi
class EventActivity : BaseActivity() {

    override val binding: ActivityEventBinding by viewBinding()

    override fun setup() {
        binding
        setActionBarTitle("Event")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // if (userId.isEmpty()) { // dunno how to do this
            menuInflater.inflate(R.menu.event_info_menu, menu)
        // }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_event_edit -> logD("Edit event")//start<EventEditActivity>()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val passedEvent = intent.getSerializableExtra(EXTRA_EVENT) as Event

        val bundle = Bundle().apply { putSerializable(EXTRA_EVENT, passedEvent as Serializable) }

        val eventInfoFragment = EventInfoFragment().apply { arguments = bundle }
        setCurrentFragment(eventInfoFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.mi_info -> setCurrentFragment(
                    EventInfoFragment().apply { arguments = bundle }
                )
                R.id.mi_games -> setCurrentFragment(
                    EventGamesFragment().apply { arguments = bundle }
                )
                R.id.mi_location -> setCurrentFragment(
                    EventLocationFragment().apply { arguments = bundle }
                )
                R.id.mi_participants -> setCurrentFragment(
                    EventParticipantsFragment().apply { arguments = bundle }
                )
                R.id.mi_chat -> passedEvent.chat?.let {
                    setCurrentFragment(EventChatFragment().apply { arguments = bundle })
                }
            }
            true
        }

        // bottomNavigationView.getOrCreateBadge(R.id.mi_chat).apply {
        //     eventChatFragment?.let { number = 3 }
        // }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        val fragments = supportFragmentManager.fragments
        val currentFragment = if (fragments.isEmpty()) Fragment() else fragments.last()

        if (fragment.className != currentFragment.className) {
            supportFragmentManager.beginTransaction().apply {
                replace(binding.flFragmentEvent.id, fragment)
                commit()
            }
        }
    }
}