package com.example.tabletop.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R

class ParticipantAdapter : RecyclerView.Adapter<ParticipantAdapter.MyViewHolder>() {

    private var viewItems = emptyList<String>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(participant: String) {

        }
    }

    //todo row_participant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_game, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val participant = viewItems[position]
        viewHolder.bind(participant)
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<String>) {
        viewItems = newList
        notifyDataSetChanged()
    }
}