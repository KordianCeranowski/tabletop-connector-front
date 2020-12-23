package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentEventGamesBinding
import com.example.tabletop.main.adapter.GameAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.util.className
import com.example.tabletop.util.getMockGame
import net.alexandroid.utils.mylogkt.logI

class EventGamesFragment : BaseFragment(R.layout.fragment_event_games) {

    override val binding: FragmentEventGamesBinding by viewBinding()

    private val gameAdapter by lazy { GameAdapter() }

    fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = gameAdapter
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        logI("Created ${this.className}")

        val games = (arguments?.getSerializable("EVENT") as Event).games

        gameAdapter.setData(games)
    }
}