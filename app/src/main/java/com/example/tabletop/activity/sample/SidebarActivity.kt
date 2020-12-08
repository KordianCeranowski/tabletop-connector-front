package com.example.tabletop.activity.sample

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivitySidebarBinding
import com.example.tabletop.util.runLoggingConfig
import kotlinx.android.synthetic.main.activity_sidebar.*
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class SidebarActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySidebarBinding

    private lateinit var toggle: ActionBarDrawerToggle

    private fun setup() {
        runLoggingConfig()

        binding = ActivitySidebarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.miItem1 -> toast("Clicked item 1")
                R.id.miItem2 -> toast("Clicked item 2")
                R.id.miItem3 -> toast("Clicked item 3")
            }
            true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}