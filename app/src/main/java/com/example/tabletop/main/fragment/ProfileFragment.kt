package com.example.tabletop.main.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.Log.ERROR
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentProfileBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Profile
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.mvvm.viewmodel.UserViewModel.getProfile
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.getFullResponse
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response
import splitties.views.InputType.Companion.text

@Suppress("COMPATIBILITY_WARNING")
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    override val binding: FragmentProfileBinding by viewBinding()

    private lateinit var settingsManager: SettingsManager

    fun setup() {
        binding
        settingsManager = SettingsManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        lifecycleScope.launch {
            settingsManager
                .userAccessTokenFlow
                .asLiveData()
                .observe(viewLifecycleOwner) { authToken ->
                    UserViewModel.run {
                        getProfile(authToken)
                        responseGetProfile.observe(viewLifecycleOwner) {
                            handleResponse(it)
                        }
                    }
                }
        }
    }

    private fun handleResponse(response: Response<Profile>){
        if (response.isSuccessful) {
            handleSuccessfulResponse(response)
        } else {
            handleErrorResponse(response)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleSuccessfulResponse(response: Response<Profile>) {
        val profile = response.body()
        logI(profile.toString())
        binding.tvProfileFirstname.text = profile?.firstName
        binding.tvProfileLastname.text = profile?.lastName
        binding.tvProfileId.text = profile?.id

        logI("Pobrano dane profilu")
    }

    private fun handleErrorResponse(response: Response<Profile>){
        logE("Pobieranie profilu nie działa, najpewniej nie jestes zalogowany")
        // logE(response.getErrorBodyProperties().toString())
        // logE(response.getFullResponse())
    }

}