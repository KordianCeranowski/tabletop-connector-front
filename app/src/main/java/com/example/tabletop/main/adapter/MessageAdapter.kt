package com.example.tabletop.main.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.R.drawable.chat_message_background_friend
import com.example.tabletop.R.drawable.chat_message_background_you
import com.example.tabletop.mvvm.model.helpers.Message
import com.example.tabletop.util.Extra
import kotlinx.android.synthetic.main.row_message.view.*
import net.alexandroid.utils.mylogkt.logI


class MessageAdapter(): RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    private var viewItems = emptyList<Message>()
    var userIdMarker: String = "You"
    var activity: Activity? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(message: Message) {
            itemView.apply {
                tv_message.text = message.message

                if (message.handle == userIdMarker) {
                    tv_sender.text = "Ty polaku brudasie"
                    user_block.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                    )
                    user_color.background = resources.getDrawable(chat_message_background_you)
                } else {
                    tv_sender.text = message.handle
                    user_block.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0.0f
                    )
                    user_color.background = resources.getDrawable(chat_message_background_friend)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_message, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val message = viewItems[position]
        viewHolder.bind(message)
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun getData(): List<Message> {
        return viewItems
    }

    fun addData(addedViewItems: List<Message>, view: View){
        viewItems = viewItems + addedViewItems
        view.post {
            notifyDataSetChanged()
        }
    }
}
