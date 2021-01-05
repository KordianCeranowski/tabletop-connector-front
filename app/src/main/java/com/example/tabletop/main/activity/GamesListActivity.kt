package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.databinding.ActivityGamesListBinding
import com.example.tabletop.main.adapter.SearchGameAdapter
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.EndlessRecyclerViewScrollListener
import com.example.tabletop.mvvm.model.helpers.Many
import com.example.tabletop.mvvm.viewmodel.GameViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response


class GamesListActivity : BaseActivity() {
    override val binding: ActivityGamesListBinding by viewBinding()
    private lateinit var settingsManager: SettingsManager
    private var nextPage: String? = null
    private val gameAdapter by lazy { SearchGameAdapter() }
    private val gameViewModel by lazyViewModels { GameViewModel() }

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        binding.recyclerView.apply {
            val l = LinearLayoutManager(applicationContext)
            layoutManager = l
            adapter = gameAdapter
            val scrollListener = object : EndlessRecyclerViewScrollListener(l) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    loadMoreData()
                }
            }
            addOnScrollListener(scrollListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        logI("GamesListActivity started")
        searchGames("")
        binding.etSearch.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                searchGames(binding.etSearch.text.toString())
                return@OnKeyListener true
            }
            false
        })

        supportActionBar?.title = "Games"
    }

    private fun updateNextPage(it: Response<Many<Game>>) {
        nextPage = it.body()?.next
        if (nextPage != null) {
            val nextNN = nextPage!!
            val endPosition = nextNN.toList().zip(nextNN.indices).filter{it.first == '&'}[0].second
            // todo
            nextPage = nextNN.substring(37, endPosition)
        }
    }

    private fun getAccessToken(): String {
        return runBlocking { settingsManager.userAccessTokenFlow.first() }
    }

    fun loadMoreData() {
        if (nextPage == null){
            logI("Wont load, next page is null")
            return
        }
        logI("Loading new games")
        logI("nextpage = $nextPage")
        gameViewModel.getNextPage(getAccessToken(), nextPage!!)
        gameViewModel.responseManyNext.observe(this) {
            val onSuccess = {
                logI("OnSuccess")
                val games = it.body()?.results ?: emptyList()
                updateNextPage(it)
                gameAdapter.addGames(games)
            }

            val onFailure = {
                logI("OnFailure")
                logW(it.getFullResponse())
            }
            it.resolve(onSuccess, onFailure)
        }

    }

    private fun searchGames(name: String) {
        logI("searchGames")
        gameViewModel.getMany(getAccessToken(), name)
        gameViewModel.responseMany.observe(this) {
            val onSuccess = {
                val games = it.body()?.results ?: emptyList()
                gameAdapter.setData(games)
                updateNextPage(it)
            }

            val onFailure = {
                logI("OnFailure")
                logW(it.getFullResponse())
                logW(it.getErrorJson().toString())
            }

            logI("Resolve")
            it.resolve(onSuccess, onFailure)
        }
    }
}
