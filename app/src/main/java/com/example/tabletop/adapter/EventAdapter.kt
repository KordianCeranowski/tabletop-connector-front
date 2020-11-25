package com.example.tabletop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.model.Event
import kotlinx.android.synthetic.main.row_event.view.*

class EventAdapter : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    private var viewItems = emptyList<Event>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_event, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tvRowEventName.text = viewItems[position].name
        //holder.itemView.id_txt.text = viewItems[position].date.toString()
        // holder.itemView.title_txt.text = viewItems[position].title
        // holder.itemView.body_txt.text = viewItems[position].body
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<Event>) {
        viewItems = newList
        notifyDataSetChanged()
    }
}