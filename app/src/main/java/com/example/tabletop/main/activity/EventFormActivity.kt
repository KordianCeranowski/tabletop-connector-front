package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.LoginForm
import com.example.tabletop.mvvm.model.helpers.LoginResponse
import com.example.tabletop.mvvm.repository.EventRepository
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.Helpers.getErrorBodyProperties
import com.example.tabletop.util.Helpers.getFullResponse
import com.example.tabletop.util.Helpers.getMockEvent
import com.example.tabletop.util.Helpers.startWithExtra
import com.google.android.gms.tasks.Tasks.await
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import retrofit2.Response
import splitties.activities.start
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
@Suppress("COMPATIBILITY_WARNING")
class EventFormActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityEventFormBinding by viewBinding()

    override lateinit var errorBodyProperties: Map<String, String>

    private val eventViewModel: EventViewModel by lazyViewModels { EventViewModel(EventRepository) }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        saveEvent(getMockEvent())
    }

    private fun saveEvent(event: Event) {
        lifecycleScope.launch {
            settingsManager.userAccessTokenFlow
            .asLiveData()
            .observe(this@EventFormActivity) { authToken ->

                eventViewModel.run {
                    save(authToken, event)
                    responseOne.observe(this@EventFormActivity) { response ->
                        if (response.isSuccessful) {
                            handleSuccessfulResponse(response)
                        } else {
                            handleErrorResponse(response)
                        }
                    }
                }

            }
        }
    }

    private fun handleSuccessfulResponse(response: Response<Event>) {
        logD(response.getFullResponse())

        lifecycleScope.launch {
            response.body()?.let {
                startWithExtra<EventActivity>("EVENT" to it)
                // or startActivity<MyEventsActivity>()
                finish()
            }
        }
    }

    private fun handleErrorResponse(response: Response<Event>) {
        logE(response.getFullResponse())
        if (!(this::errorBodyProperties.isInitialized)) {
            errorBodyProperties = response.getErrorBodyProperties()
        }
        logD(errorBodyProperties.toString())

        val errors = mapOf(
            "name" to listOf("This field is required."),
            "date" to listOf("This field is required."),
            "address" to listOf("This field is required."),
        )

        if (errorBodyProperties["name"] == errors.getValue("name").first()) {
            toast("Invalid credentials")
        } else {
            toast("Something went wrong")
        }
    }
}