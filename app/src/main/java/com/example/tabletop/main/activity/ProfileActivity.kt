package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityProfileBinding
import com.example.tabletop.mvvm.model.helpers.Profile
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.resolve
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response

@Suppress("COMPATIBILITY_WARNING")
class ProfileActivity : BaseActivity() {

    override val binding: ActivityProfileBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Profile")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserver()

        lifecycleScope.launch {
            val accessToken = settingsManager.userAccessTokenFlow.first()
            retrieveProfile(accessToken)
        }
    }

    private fun attachObserver() {
        UserViewModel.responseGetProfile.observe(this) { handleResponse(it) }
    }

    private fun retrieveProfile(accessToken: String) {
        val profileId = intent.getStringExtra("PROFILE_ID") ?: ""

        if (profileId.isEmpty()) {
            UserViewModel.getMyProfile(accessToken)
        } else {
            //todo: test
            UserViewModel.getProfile(profileId)
        }
    }

    private fun handleResponse(response: Response<Profile>){

        val onSuccess = {
            val profile = response.body()!!
            logI(profile.toString())
            binding.tvProfileFirstname.text = profile.firstname
            binding.tvProfileLastname.text = profile.lastname
            binding.tvProfileId.text = profile.id
            //binding.ivProfileAvatar.setImageURI(Uri.parse(profile.avatar))

            logI("Pobrano dane profilu")
        }

        val onFailure = {
            logE("Pobieranie profilu nie działa, najpewniej nie jestes zalogowany")
            response.getErrorBodyProperties()
        }

        response.resolve(onSuccess, onFailure)
    }
}