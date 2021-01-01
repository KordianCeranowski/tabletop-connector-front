package com.example.tabletop.main.adapter

import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.main.activity.EventActivity
import com.example.tabletop.mvvm.model.Event
import com.example.tabletop.util.Extra
import com.example.tabletop.util.startWithExtra
import kotlinx.android.synthetic.main.row_event.view.*


@Suppress("EXPERIMENTAL_API_USAGE")
class EventAdapter : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    private var events = emptyList<Event>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: Event) {
            itemView.apply {
                row_event_name.text = event.name
                row_event_date.text = event.date
                row_event_participants.text = event.participants.size.toString()
                //row_event_distance.text = TODO()
            }

            itemView.setOnClickListener {
                itemView.context.startWithExtra<EventActivity>(
                    Extra.EVENT.toString() to event
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_event, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val event = events[position]
        viewHolder.bind(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun setData(newList: List<Event>) {
        events = newList
        notifyDataSetChanged()
    }
}