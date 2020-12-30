package com.example.tabletop.main.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.R
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

//TODO: DO NOT USE
class ProfileFragment : BaseFragment(R.layout.activity_profile) {

    override val binding = TODO()

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        binding
        settingsManager = SettingsManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        attachObserver()

        lifecycleScope.launch {
            val accessToken = settingsManager.userAccessTokenFlow.first()
            retrieveProfile(accessToken)
        }
    }

    private fun attachObserver() {
        UserViewModel.responseGetProfile.observe(viewLifecycleOwner) { handleResponse(it) }
    }

    private fun retrieveProfile(accessToken: String) {
        val isMyProfile = arguments?.getBoolean("IS_MY_PROFILE")!!
        val profileId = arguments?.getBoolean("PROFILE_ID")!!

        if (isMyProfile) {
            UserViewModel.getMyProfile(accessToken)
        } else {
            //UserViewModel.getProfile(id)
        }
    }

    private fun handleResponse(response: Response<Profile>){

        val onSuccess = {
            val profile = response.body()!!
            logI(profile.toString())
            // binding.tvProfileFirstname.text = profile.firstname
            // binding.tvProfileLastname.text = profile.lastname
            // binding.tvProfileId.text = profile.id
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