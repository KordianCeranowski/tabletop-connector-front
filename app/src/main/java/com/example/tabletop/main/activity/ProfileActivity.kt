package com.example.tabletop.main.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityProfileBinding
import com.example.tabletop.mvvm.model.helpers.Profile
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logV
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start

@Suppress("COMPATIBILITY_WARNING")
class ProfileActivity : BaseActivity() {

    override val binding: ActivityProfileBinding by viewBinding()

    private val userViewModel by lazyViewModels { UserViewModel() }

    private lateinit var settingsManager: SettingsManager

    private lateinit var profileId: String

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Profile")

        profileId = intent.getStringExtra(Extra.PROFILE_ID()) ?: ""
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (profileId.isEmpty()) {
            menuInflater.inflate(R.menu.profile_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_profile_edit -> start<ProfileEditActivity>()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserver()

        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        retrieveProfile(accessToken)
    }

    private fun attachObserver() {
        userViewModel.responseGetProfile.observe(this) { handleResponse(it) }
    }

    private fun retrieveProfile(accessToken: String) {
        if (profileId.isEmpty()) {
            userViewModel.getMyProfile(accessToken)
        } else {
            userViewModel.getProfile(accessToken, profileId)
        }
    }

    private fun handleResponse(response: Response<Profile>){
        val onSuccess = {
            if (response.body() != null) {
                val profile = response.body()!!
                binding.tvProfileFullname.text =
                    StringBuilder()
                        .append(profile.firstname)
                        .append(" ")
                        .append(profile.lastname)
                        .toString()
                binding.ivProfileAvatar.setImageFromURL(this, profile.avatar)
            } else {
                logW("Response body is empty")
            }
        }

        val onFailure = {
            logW(response.getFullResponse())
            response.getErrorBodyProperties()
        }

        response.resolve(onSuccess, onFailure)
    }
}