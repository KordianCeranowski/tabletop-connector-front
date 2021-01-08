package com.example.tabletop.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.main.activity.EventActivity
import com.example.tabletop.mvvm.model.helpers.EventWrapper
import com.example.tabletop.util.*
import kotlinx.android.synthetic.main.row_event.view.*

@Suppress("EXPERIMENTAL_API_USAGE")
class EventAdapter : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    private var wrappedEvents = emptyList<EventWrapper>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(eventWrapper: EventWrapper) {
            val roundedDistanceInKilometers = (eventWrapper.distance * 0.001).format("%.1f")

            val roundedDistanceInMeters = (roundedDistanceInKilometers * 1000)

            val roundedDistanceInKilometersNoDecimals =
                roundedDistanceInKilometers.format("%.0f")

            val (date, _) = getSeparatedDateTime(eventWrapper.event.date)

            itemView.apply {
                row_event_name.text = eventWrapper.event.name
                row_event_date.text = date

                row_event_distance.text = when {
                    roundedDistanceInKilometers < 1.0 -> {
                        "${roundedDistanceInMeters.toInt()} m"
                    }
                    roundedDistanceInKilometers > 10.0 -> {
                        "${roundedDistanceInKilometersNoDecimals.toInt()} Km"
                    }
                    else -> {
                        "$roundedDistanceInKilometers Km"
                    }
                }

                setOnClickListener {
                    context.startWithExtra<EventActivity>(
                        //todo send eventWrapper and show distance in EventInfoFragment
                        Extra.EVENT() to eventWrapper.event
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_event, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val event = wrappedEvents[position]
        viewHolder.bind(event)
    }

    override fun getItemCount(): Int {
        return wrappedEvents.size
    }

    fun setData(newList: List<EventWrapper>) {
        wrappedEvents = newList
        notifyDataSetChanged()
    }
}