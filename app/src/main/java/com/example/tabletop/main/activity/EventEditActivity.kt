package com.example.tabletop.main.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.observe
import com.example.tabletop.R
import com.example.tabletop.databinding.ActivityEventEditBinding
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.mvvm.viewmodel.EventViewModel
import com.example.tabletop.settings.SettingsManager
import com.example.tabletop.util.*
import dev.ajkueterman.lazyviewmodels.lazyViewModels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.alexandroid.utils.mylogkt.logD
import net.alexandroid.utils.mylogkt.logE
import retrofit2.Response
import splitties.activities.start
import splitties.toast.toast


@Suppress("COMPATIBILITY_WARNING", "EXPERIMENTAL_API_USAGE")
class EventEditActivity : BaseActivity() {

    override val binding: ActivityEventEditBinding by viewBinding()

    private val eventViewModel by lazyViewModels { EventViewModel() }

    private lateinit var settingsManager: SettingsManager

    override fun setup() {
        settingsManager = SettingsManager(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()

        attachObserverDeleteEvent()

        val accessToken = runBlocking { settingsManager.userAccessTokenFlow.first() }
        val eventId = intent.getStringExtra(Extra.EVENT_ID())!!

        binding.btnDeleteEvent.setOnClickListener {
            showAlertDialogDeleteEvent(accessToken, eventId)
        }
    }

    private fun showAlertDialogDeleteEvent(accessToken: String, eventId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm")

        val customLayout = layoutInflater.inflate(R.layout.alert_dialog_delete_event, null)
        builder.setView(customLayout)

        builder.setPositiveButton("OK") { _, _ ->
            deleteEvent(accessToken, eventId)
        }

        builder.setNegativeButton("Cancel") { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteEvent(accessToken: String, eventId: String) {
        eventViewModel.remove(accessToken, eventId)
    }

    private fun attachObserverDeleteEvent() {
        eventViewModel.responseOne.observe(this) { handleResponse(it) }
    }

    private fun handleResponse(response: Response<Event>) {
        val onSuccess = {
            logD(response.status())

            toast("Event deleted!")

            // val intent = Intent(applicationContext, MainActivity::class.java)
            // intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // startActivity(intent)
            finishAffinity()
            start<MainActivity>()
        }

        val onFailure = {
            logE(response.status())
            toast(ERROR_MESSAGE_FAILURE)
        }

        response.resolve(onSuccess, onFailure)
    }
}