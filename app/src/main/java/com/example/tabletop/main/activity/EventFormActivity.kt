package com.example.tabletop.main.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.databinding.ActivityEventFormBinding
import com.example.tabletop.main.adapter.ChosenGameAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
class EventFormActivity : BaseActivity() {

    override val binding: ActivityEventFormBinding by viewBinding()

    private val eventViewModel by lazyViewModels { EventViewModel() }

    private lateinit var settingsManager: SettingsManager

    private val gameAdapter by lazy { ChosenGameAdapter() }


    override fun setup() {

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = gameAdapter
        }

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

        binding.btnDate.setOnClickListener { handleDateClick() }
        binding.btnTime.setOnClickListener { handleTimeClick() }
        binding.btnAutofill.setOnClickListener { handleAddressClick() }
        binding.btnAdd.setOnClickListener { handleGamesClick() }
        binding.btnSubmit.setOnClickListener { handleSubmitClick()}


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

        val (longitude, latitude) = getCurrentLocation()

        val geocoder = Geocoder(this, Locale.getDefault())

        val loc = geocoder.getFromLocation(latitude, longitude, 1)

        if (loc == null || loc.size == 0) {
            toast("Couldn't access location")
            logI(loc.toString())
        }
        else {
            val address = geocoder.getFromLocation(latitude, longitude, 1)[0]
            // val addr = Address(country, city, street, postalCode, number, null, null)

            binding.tfCountry.setText(address.countryName)
            binding.tfCity.setText(address.locality)
            binding.tfPostal.setText(address.thoroughfare)
            binding.tfStreet.setText(address.postalCode)
            binding.tfNumber.setText(address.featureName)
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
                val result = data?.extras?.get("game") as Game
                gameAdapter.addGame(result)
                logI("recived ${result.toString()}")
            }
        }
    }

    private fun handleSubmitClick() {
        val date = binding.btnDate.text.toString()
        val time = binding.btnTime.text.toString()
        val address = Address(
            binding.tfCountry.text.toString(),
            binding.tfCity.text.toString(),
            binding.tfStreet.text.toString(),
            binding.tfPostal.text.toString(),
            binding.tfNumber.text.toString(),
            null,null
        )
        val games = gameAdapter.getGames()

    }

    // Save Event
    private fun saveEvent(accessToken: String, eventRequest: EventRequest) {
        eventViewModel.save(accessToken, eventRequest)
    }

    private fun attachObserver() {
        eventViewModel.responseOne.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Event>) {
        val onSuccess = {
            logD(response.getFullResponse())

            response.body()?.let {
                //startWithExtra<EventActivity>(EXTRA_EVENT to it)
                startWithExtra<MainActivity>(Extra.IS_MY_EVENTS() to true)
                finish()
            } as Unit
        }

        val onFailure = {
            val errorJson = response.getErrorJson()

            logE(response.getFullResponse())
            logD(errorJson.toString())
        }

        response.resolve(onSuccess, onFailure)
    }
}
