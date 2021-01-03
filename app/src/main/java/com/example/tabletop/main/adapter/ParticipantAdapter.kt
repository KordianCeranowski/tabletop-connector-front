package com.example.tabletop.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.main.activity.ProfileActivity
import com.example.tabletop.mvvm.model.User
import com.example.tabletop.util.Extra
import com.example.tabletop.util.setImageFromURL
import com.example.tabletop.util.startWithExtra
import kotlinx.android.synthetic.main.row_participant.view.*

class ParticipantAdapter : RecyclerView.Adapter<ParticipantAdapter.MyViewHolder>() {

    private var viewItems = emptyList<User>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.run {
                row_participant_name.text =
                    StringBuilder()
                        .append(user.profile.firstname)
                        .append(" ")
                        .append(user.profile.lastname)
                        .toString()

                row_participant_profile_picture.setImageFromURL(context, user.profile.avatar)
            }

            itemView.setOnClickListener {
                itemView.context.startWithExtra<ProfileActivity>(
                    Extra.PROFILE_ID.toString() to user.profile.id
                )
            }
        }
    }

    //todo row_participant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_participant, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val user = viewItems[position]
        viewHolder.bind(user)
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<User>) {
        viewItems = newList
        notifyDataSetChanged()
    }
}