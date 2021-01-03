package com.example.tabletop.main.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.tabletop.databinding.ActivityGamesListBinding
import com.example.tabletop.settings.SettingsManager
import com.livinglifetechway.k4kotlin.core.toast
import net.alexandroid.utils.mylogkt.logI


class GamesListActivity : BaseActivity() {
    override val binding: ActivityGamesListBinding by viewBinding()
    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
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

        val data = Intent()
        data.putExtra("gamename", "XDD")
        setResult(Activity.RESULT_OK, data)

        val numbers = listOf("one", "two", "three", "four")
        binding.lvGames.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, numbers)

        binding.lvGames.setOnItemClickListener { parent, view, position, id ->
            data.putExtra("gamename", numbers[position])
            finish()
        }
    }
}
