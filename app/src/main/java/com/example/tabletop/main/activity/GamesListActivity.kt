package com.example.tabletop.main.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.databinding.ActivityGamesListBinding
import com.example.tabletop.main.adapter.EventAdapter
import com.example.tabletop.main.adapter.GameAdapter
import com.example.tabletop.main.adapter.SearchGameAdapter
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.getMockGame
import com.livinglifetechway.k4kotlin.core.toast
import net.alexandroid.utils.mylogkt.logI


class GamesListActivity : BaseActivity() {
    override val binding: ActivityGamesListBinding by viewBinding()
    private lateinit var settingsManager: SettingsManager
    private val gameAdapter by lazy { SearchGameAdapter() }

    override fun setup() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            gameAdapter.setData(listOf(getMockGame(),getMockGame(),getMockGame()))
            adapter = gameAdapter
        }
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Games")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        logI("GamesListActivity started")
        //
        // val data = Intent()
        // data.putExtra("gamename", "XDD")
        // setResult(Activity.RESULT_OK, data)
    }
}
