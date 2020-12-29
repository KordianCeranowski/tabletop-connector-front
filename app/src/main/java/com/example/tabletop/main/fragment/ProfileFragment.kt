package com.example.tabletop.main.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.FragmentProfileBinding
import com.example.tabletop.main.activity.MainActivity
import com.example.tabletop.mvvm.model.helpers.Profile
import com.example.tabletop.mvvm.viewmodel.UserViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.getErrorBodyProperties
import com.example.tabletop.util.getFullResponse
import com.example.tabletop.util.resolve
import com.example.tabletop.util.status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import net.alexandroid.utils.mylogkt.logW
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast

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

        attachProfile()
    }

    private fun attachProfile(){
        lifecycleScope.launch {
            settingsManager
                .userAccessTokenFlow
                .asLiveData()
                .observe(viewLifecycleOwner) { getProfile(it) }
        }
    }

    private fun getProfile(accessToken: String) {
        var isAlreadyHandled = false
        UserViewModel.run {
            getProfile(accessToken)
            responseGetProfile.observe(viewLifecycleOwner) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponse(it)
                }
            }
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
            logE(response.getErrorBodyProperties().toString())
        }

        response.resolve(onSuccess, onFailure)
    }
}