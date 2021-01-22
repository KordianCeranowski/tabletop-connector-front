package com.example.tabletop.main.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import android.widget.TextView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response
import splitties.toast.UnreliableToastApi
import splitties.toast.toast
import java.text.SimpleDateFormat
import java.util.*


@UnreliableToastApi
@SuppressLint("SimpleDateFormat", "SetTextI18n")
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
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
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

        binding.btnDate.apply {
            text = getCurrentDate()
            setOnClickListener { handleDateClick() }
        }
        binding.btnTime.apply {
            text = getCurrentTime()
            setOnClickListener { handleTimeClick() }
        }
        binding.btnAutofill.setOnClickListener { handleAddressClick() }
        binding.btnAdd.setOnClickListener { handleGamesClick() }
        binding.btnSubmit.setOnClickListener { handleSubmitClick() }

        attachObserver()

        //val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        //saveEvent(accessToken, getMockEventRequest())
    }

    // Handling
    private fun handleDateClick() {
        val calendar = Calendar.getInstance()

        val initialDate = object {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val date = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

            binding.btnDate.text = date
        }

        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener, initialDate.year, initialDate.month, initialDate.day
        )
        datePickerDialog.show()
    }

    private fun handleTimeClick() {
        logI("Clicked Time")

        val calendar = Calendar.getInstance()

        val initialTime = object {
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            val time = SimpleDateFormat("HH:mm").format(calendar.time)
            binding.btnTime.text = time
        }

        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener, initialTime.hour, initialTime.minute,
            true
        )
        timePickerDialog.show()
    }

    private fun handleAddressClick() {
        logI("Clicked Address")

        val (longitude, latitude) = getCurrentLocation()

        val geocoder = Geocoder(this, Locale.getDefault())

        val loc = geocoder.getFromLocation(latitude, longitude, 1)

        if (loc == null || loc.size == 0) {
            toast("Couldn't access location")
            logI(loc.toString())
        } else {
            val address = geocoder.getFromLocation(latitude, longitude, 1)[0]
            binding.tfCountry.setText(address.countryName)
            binding.tfCity.setText(address.locality)
            binding.tfPostal.setText(address.postalCode)
            binding.tfStreet.setText(address.thoroughfare)
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
                logI("Received $result")
            }
        }
    }

    private fun handleSubmitClick() {
        val name = binding.tfName.text.toString()
        val date = binding.btnDate.text.toString() + "T" + binding.btnTime.text.toString() + ":00+0000"
        val address = Address(
            binding.tfCountry.text.toString(),
            binding.tfCity.text.toString(),
            binding.tfStreet.text.toString(),
            binding.tfPostal.text.toString(),
            binding.tfNumber.text.toString(),
            null,null
        )
        val games = gameAdapter.getGames()

        val eventRequest = EventRequest(name, date, address, games)

        logI(eventRequest.toString())
        eventViewModel.save(getAccessToken(this), eventRequest)
    }

    private fun attachObserver() {
        eventViewModel.responseOne.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Event>) {
        val onSuccess = {
            logD(response.getFullResponse())

            response.body()?.let {
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
