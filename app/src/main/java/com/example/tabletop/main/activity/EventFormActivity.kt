package com.example.tabletop.main.activity

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import im.delight.android.location.SimpleLocation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response
import splitties.activities.start
import splitties.permissions.requestPermission
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.text.SimpleDateFormat
import java.util.*


@UnreliableToastApi
@Suppress("COMPATIBILITY_WARNING")
class EventFormActivity : BaseActivity(), IErrorBodyProperties {

    override val binding: ActivityEventFormBinding by viewBinding()

    private val eventViewModel by lazyViewModels { EventViewModel() }

    override lateinit var errorBodyProperties: Map<String, String>

    private lateinit var settingsManager: SettingsManager

    private val games = mutableListOf<String>()

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun setup() {
        binding
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("New Event")
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        logI("Opened EventFormActivity.OnCreate")

        binding.listGames.adapter = ArrayAdapter(this, R.layout.simple_list_item_1, games)

        binding.btnDate.setOnClickListener { handleDateClick() }
        binding.btnTime.setOnClickListener { handleTimeClick() }
        binding.btnAutofill.setOnClickListener { handleAddressClick() }
        binding.btnAdd.setOnClickListener { handleGamesClick() }


        attachObserver()

        //val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        //saveEvent(accessToken, getMockEventRequest())
    }

    // Handling
    private fun handleDateClick() {
        logI("Clicked Date")

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                view, mYear, mMonth, mDay -> binding.btnDate.text = "$mDay/$mMonth/$mYear"
        }, year, month, day)

        dpd.show()
    }

    @SuppressLint("SimpleDateFormat")
    private fun handleTimeClick() {
        logI("Clicked Time")

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            binding.btnTime.text = SimpleDateFormat("HH:mm").format(c.time)
        }
        TimePickerDialog(this, timeSetListener, hour, minute, true).show()
    }

    private fun handleAddressClick() {
        logI("Clicked Address")

        lifecycleScope.launch {
            requestPermission(ACCESS_FINE_LOCATION)
            val location = SimpleLocation(this@EventFormActivity)
            location.beginUpdates()
            if (!location.hasLocationEnabled()) {
                // ask the user to enable location access
                SimpleLocation.openSettings(this@EventFormActivity)
            }
            val latitude = location.latitude
            val longitude = location.longitude
            logI("lat $latitude, long $longitude")
            location.endUpdates()

            val geocoder = Geocoder(this@EventFormActivity, Locale.getDefault())
            val loc = geocoder.getFromLocation(latitude, longitude, 1)
            if (loc == null || loc.size == 0){
                toast("Couldn't access location")
                logI(loc.toString())
            }
            else {
                val address = geocoder.getFromLocation(latitude, longitude, 1)[0]

                val country = address.countryName
                val city = address.locality
                val street = address.thoroughfare
                val postalCode = address.postalCode
                val number = address.featureName

                val addr = Address(country, city, street, postalCode, number, latitude, longitude)

                binding.tfCountry.setText(country)
                binding.tfCity.setText(city)
                binding.tfPostal.setText(postalCode)
                binding.tfStreet.setText(street)
                binding.tfNumber.setText(number)

                logI(addr.toString())
            }
        }
    }

    private fun handleGamesClick() {
        logI("Clicked Games")
        val intent = Intent(this, GamesListActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.extras?.get("gamename")
                games.add(result.toString())
                logI(games.toString())
                binding.listGames.adapter = ArrayAdapter(this, R.layout.simple_list_item_1, games)
            }
        }
    }

    // Save Event
    private fun saveEvent(accessToken: String, eventRequest: EventRequest) {
        eventViewModel.save(accessToken, eventRequest)
    }

    private fun attachObserver() {
        eventViewModel.responseOne.observe(this@EventFormActivity) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Event>) {
        val onSuccess = {
            logD(response.getFullResponse())

            val userId = runBlocking { settingsManager.userIdFlow.first() }

            response.body()?.let {
                //startWithExtra<EventActivity>(EXTRA_EVENT to it)
                startWithExtra<MainActivity>(Extra.IS_MY_EVENTS() to true)
                finish()
            } as Unit
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