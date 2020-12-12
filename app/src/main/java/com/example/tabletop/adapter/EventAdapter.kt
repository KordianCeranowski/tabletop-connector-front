package com.example.tabletop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.model.Event
import com.livinglifetechway.k4kotlin.core.onClick
import kotlinx.android.synthetic.main.row_event.view.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var viewItems = emptyList<Event>()

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = viewItems[position]
        holder.itemView.apply {
            // Event entity attributes
            row_event_name.text = event.name
            row_event_date.text = event.date
            row_event_participants.text = event.participants.size.toString()
            //row_event_distance.text =
            //todo: do this but on the whole row_event view
            //row_event_name.onClick {  }
        }
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<Event>) {
        viewItems = newList
        notifyDataSetChanged()
    }
}