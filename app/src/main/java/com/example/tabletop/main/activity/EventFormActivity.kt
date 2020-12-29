package com.example.tabletop.main.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.location.Geocoder
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import kotlinx.coroutines.launch
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.text.SimpleDateFormat
import java.util.*

@UnreliableToastApi
@Suppress("COMPATIBILITY_WARNING")
class EventFormActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityEventFormBinding by viewBinding()

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        logI("Opened EventFormActivity.OnCreate")

        binding.tvEventDate.setOnClickListener{ handleDateClick() }
        binding.tvEventTime.setOnClickListener{ handleTimeClick() }
        binding.tvEventAddress.setOnClickListener{ handleAddressClick() }
        binding.tvEventGames.setOnClickListener{ handleGamesClick() }


        lifecycleScope.launch {
            settingsManager.userAccessTokenFlow
                .asLiveData()
                .observe(this@EventFormActivity) { saveEvent(it, getMockEvent()) }
        }
    }

    private fun handleDateClick() {
        logI("Clicked Date")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                view, mYear, mMonth, mDay -> binding.tvEventDate.text = "$mDay/$mMonth/$mYear"
        }, year, month, day)

        dpd.show()
    }

    private fun handleTimeClick() {
        logI("Clicked Time")

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            binding.tvEventTime.text = SimpleDateFormat("HH:mm").format(c.time)
        }
        TimePickerDialog(this, timeSetListener, hour, minute, true).show()
    }

    private fun handleAddressClick() {
        logI("Clicked Address")
        val lat = 54.4908183
        val long = 18.5116491

        val geocoder = Geocoder(this, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, long, 1)[0]

        val country = address.countryName
        val city = address.locality
        val street =  address.thoroughfare
        val postalCode = address.postalCode
        val number = address.featureName

        val addr = Address(country, city, street, postalCode, number, lat, long)

        logI(addr.toString())
    }

    private fun handleGamesClick() {
        logI("Clicked Games")
    }

    private fun saveEvent(accessToken: String, event: Event) {
        var isAlreadyHandled = false
        EventViewModel.run {
            save(accessToken, event)
            responseOne.observe(this@EventFormActivity) {
                if (!(isAlreadyHandled)) {
                    isAlreadyHandled = true
                    handleResponse(it)
                }
            }
        }
    }

    private fun handleResponse(response: Response<Event>) {

        val onSuccess = {
            logD(response.getFullResponse())

            lifecycleScope.launch {
                response.body()?.let {
                    startWithExtra<EventActivity>("EVENT" to it)
                    // or startActivity<MyEventsActivity>()
                    finish()
                }
            }
        }

        val onFailure = {
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

        response.resolve(onSuccess, onFailure)
    }
}