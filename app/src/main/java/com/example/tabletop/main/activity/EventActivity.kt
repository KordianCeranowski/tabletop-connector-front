package com.example.tabletop.main.activity

import android.os.Bundle
import android.text.Layout
import android.viewbinding.library.activity.viewBinding
import androidx.fragment.app.Fragment
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventBinding
import com.example.tabletop.main.fragment.*
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.User
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        val passedEvent = intent.getSerializableExtra("EVENT") as Event

        val bundle = Bundle().apply { putSerializable("EVENT", passedEvent as Serializable) }

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
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flFragmentEvent.id, fragment)
            commit()
        }
    }
}