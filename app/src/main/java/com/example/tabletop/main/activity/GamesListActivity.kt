package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.databinding.ActivityGamesListBinding
import com.example.tabletop.main.adapter.SearchGameAdapter
import com.example.tabletop.mvvm.viewmodel.GameViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.getFullResponse
import com.example.tabletop.util.resolve
import com.example.tabletop.util.status
import com.example.tabletop.util.*
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
            adapter = gameAdapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        logI("GamesListActivity started")
        searchGames("")
        binding.etSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                logI("inserted ${binding.etSearch.text}")
                searchGames(binding.etSearch.text.toString())
                return@OnKeyListener true
            }
            false
        })

        supportActionBar?.title = "Games"
    }

    private fun searchGames(name: String) {
        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        gameViewModel.getMany(accessToken, name)
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
                logW(it.getErrorJson().toString())
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
