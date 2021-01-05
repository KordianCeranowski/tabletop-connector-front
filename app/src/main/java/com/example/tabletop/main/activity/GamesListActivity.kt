package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.databinding.ActivityGamesListBinding
import com.example.tabletop.main.adapter.SearchGameAdapter
import com.example.tabletop.mvvm.viewmodel.GameViewModel
import com.example.tabletop.settings.SettingsManager
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
