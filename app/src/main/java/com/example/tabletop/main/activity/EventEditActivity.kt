package com.example.tabletop.main.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventEditBinding
import com.example.tabletop.main.adapter.ChosenGameAdapter
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.mvvm.model.helpers.Address
import com.example.tabletop.mvvm.model.helpers.request.EventRequest
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import com.livinglifetechway.k4kotlin.core.value
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import net.alexandroid.utils.mylogkt.logI
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast
import java.text.SimpleDateFormat
import java.util.*


@Suppress("COMPATIBILITY_WARNING", "EXPERIMENTAL_API_USAGE")
class EventEditActivity : BaseActivity() {

    override val binding: ActivityEventEditBinding by viewBinding()

    private val eventViewModel by lazyViewModels { EventViewModel() }

    private lateinit var settingsManager: SettingsManager

    private val gameAdapter by lazy { ChosenGameAdapter() }

    private lateinit var currentEvent: Event

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
        setActionBarTitle("Edit event")

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = gameAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_event_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_delete_event -> {
                showAlertDialogDeleteEvent(getAccessToken(this), currentEvent.id)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserverDeleteEvent()
        attachObserverEditEvent()

        val event = intent.getSerializableExtra(Extra.EVENT()) as Event
        currentEvent = event

        val (date, time) = getSeparatedDateTime(event.date)

        binding.run {
            tfName.value = event.name

            btnDate.apply {
                text = date
                setOnClickListener { handleDateClick() }
            }

            btnTime.apply {
                text = time
                setOnClickListener { handleTimeClick() }
            }

            val address = event.address
            address.let {
                tfCountry.value = it.country
                tfCity.value = it.city
                tfStreet.value = it.street
                tfPostal.value = it.postal_code
                tfNumber.value = it.number
            }
            gameAdapter.setData(event.games)

            btnAutofill.setOnClickListener { handleAddressClick() }
            btnAdd.setOnClickListener { handleGamesClick() }
            btnSubmit.setOnClickListener { handleSubmitClick() }
        }
    }

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

        val event = Event(
            name,
            currentEvent.creator,
            date,
            address,
            currentEvent.participants,
            games,
            id = currentEvent.id
        )

        logI(event.toString())
        editEvent(event)
    }

    private fun editEvent(event: Event) {
        eventViewModel.edit(getAccessToken(this), event.id, event)
    }

    private fun attachObserverEditEvent() {
        eventViewModel.responseOne.observe(this) {
            val onSuccess = {
                logD(it.getFullResponse())

                toast("Event updated!")

                finish()
            }

            val onFailure = {
                val errorJson = it.getErrorJson()

                logE(it.getFullResponse())
                logD(errorJson.toString())
            }

            it.resolve(onSuccess, onFailure)
        }
    }

    // Delete event
    private fun showAlertDialogDeleteEvent(accessToken: String, eventId: String) {
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Confirm")
            setMessage("Are you sure you want to delete this event?")

            setPositiveButton("OK") { _, _ ->
                deleteEvent(accessToken, eventId)
            }

            setNegativeButton("Cancel") { _, _ -> }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteEvent(accessToken: String, eventId: String) {
        eventViewModel.remove(accessToken, eventId)
    }

    private fun attachObserverDeleteEvent() {
        eventViewModel.responseOneDelete.observe(this) {
            val onSuccess = {
                logD(it.status())

                toast("Event deleted!")

                finishAffinity()
                start<MainActivity>()
            }

            val onFailure = {
                logE(it.status())
                toast(ERROR_MESSAGE_FAILURE)
            }

            it.resolve(onSuccess, onFailure)
        }
    }
}