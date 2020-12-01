package com.example.tabletop.adapter

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.model.Event
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.row_event.view.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.MockViewHolder>() {

    private var viewItems = emptyList<Event>()

    inner class MockViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_event, parent, false)
        return MockViewHolder(view)
    }

    override fun onBindViewHolder(holder: MockViewHolder, position: Int) {
        holder.itemView.apply {
            row_event_name.text = viewItems[position].name
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