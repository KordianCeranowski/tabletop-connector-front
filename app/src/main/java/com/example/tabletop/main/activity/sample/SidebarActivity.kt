package com.example.tabletop.main.activity.sample

import android.os.Bundle
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.tabletop.R
import com.example.tabletop.main.activity.BaseActivity
import com.example.tabletop.databinding.ActivitySidebarBinding
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class SidebarActivity : BaseActivity() {

    override val binding: ActivitySidebarBinding by viewBinding()

    private lateinit var toggle: ActionBarDrawerToggle

    override fun setup() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
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