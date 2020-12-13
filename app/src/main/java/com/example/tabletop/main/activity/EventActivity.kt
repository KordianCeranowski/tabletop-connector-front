package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.fragment.app.Fragment
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.model.Event
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_event.view.*
import net.alexandroid.utils.mylogkt.logI

class EventActivity : BaseActivity() {

    override val binding: ActivityEventBinding by viewBinding()

    override fun setup() {
        binding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        //todo
        //  if user is organiser of an event show EDIT icon on about screen in top right corner
        val passedEvent = intent.getSerializableExtra("EVENT") as Event

        //todo
        //  bottom navigation view bug: after changing a tab back and forth 2 times the next time
        //  the tab is accessed the layout won't be evaluated

        val eventInfoFragment = EventInfoFragment(passedEvent)
        val eventGamesFragment = EventGamesFragment(passedEvent.games)
        val eventLocationFragment = EventLocationFragment(passedEvent.address)
        val eventParticipantsFragment = EventParticipantsFragment(passedEvent.participants)
        val eventChatFragment = passedEvent.chat?.let { EventChatFragment(it) }

        setCurrentFragment(eventInfoFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.mi_info -> setCurrentFragment(eventInfoFragment)
                R.id.mi_games -> setCurrentFragment(eventGamesFragment)
                R.id.mi_location -> setCurrentFragment(eventLocationFragment)
                R.id.mi_participants -> setCurrentFragment(eventParticipantsFragment)
                R.id.mi_chat -> eventChatFragment?.let { setCurrentFragment(it) }
            }
            true
        }

        // bottomNavigationView.getOrCreateBadge(R.id.mi_chat).apply {
        //     eventChatFragment?.let { number = 3 }
        // }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }
    }









}