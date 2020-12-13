package com.example.tabletop.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.activity.EventActivity
import com.example.tabletop.activity.MainActivity
import com.example.tabletop.model.Event
import com.example.tabletop.util.Helpers.startWithExtra
import kotlinx.android.synthetic.main.row_event.view.*
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@UnreliableToastApi
class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var viewItems = emptyList<Event>()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: Event) {
            itemView.apply {
                // Event entity attributes
                row_event_name.text = event.name
                row_event_date.text = event.date
                row_event_participants.text = event.participants.size.toString()
                //row_event_distance.text =
            }

            itemView.setOnClickListener {
                itemView.context.startWithExtra<EventActivity>("EVENT" to event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: EventViewHolder, position: Int) {
        val event = viewItems[position]
        viewHolder.bind(event)

    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<Event>) {
        viewItems = newList
        notifyDataSetChanged()
    }
}