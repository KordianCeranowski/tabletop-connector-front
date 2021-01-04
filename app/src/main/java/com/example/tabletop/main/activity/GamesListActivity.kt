package com.example.tabletop.main.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityGamesListBinding
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.main.adapter.GameAdapter
import com.example.tabletop.main.adapter.SearchGameAdapter
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.mvvm.viewmodel.GameViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.toast
import dev.ajkueterman.lazyviewmodels.lazyActivityViewModels
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW


class GamesListActivity : BaseActivity() {
    override val binding: ActivityGamesListBinding by viewBinding()
    private lateinit var settingsManager: SettingsManager
    private val gameAdapter by lazy { SearchGameAdapter() }
    private val gameViewModel by lazyViewModels { GameViewModel() }

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            gameAdapter.setData(listOf(getMockGame(),getMockGame(),getMockGame()))
            adapter = gameAdapter
        }

        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        gameViewModel.getMany(accessToken)
        gameViewModel.responseMany.observe(this) {
            val onSuccess = {
                logI("OnSuccess")
                logD(it.status())
                if (it.body() == null)
                    logW("Response has empty body")
                val games = it.body()?.results ?: emptyList()
                logI(games.toString())
                it.body()?.let { gameAdapter.setData(games) } as Unit
            }

            val onFailure = {
                logI("OnFailure")
                logW(it.getFullResponse())
                logW(it.getErrorBodyProperties().toString())
            }

            logI("Resolve")
            it.resolve(onSuccess, onFailure)
        }

        setActionBarTitle("Games")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        logI("GamesListActivity started")
    }
}
