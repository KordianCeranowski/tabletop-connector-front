package com.example.tabletop.main.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.R.drawable.chat_message_background_you
import com.example.tabletop.mvvm.model.helpers.Message
import kotlinx.android.synthetic.main.row_message.view.*


class MessageAdapter: RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    private var viewItems = emptyList<Message>()
    var activity: Activity? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(message: Message) {
            itemView.apply {
                tv_message.text = message.message
                tv_sender.text = message.handle
                if (message.handle == "you"){
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1.0f
                    )
                    user_block.layoutParams = param
                    user_color.background = resources.getDrawable(R.drawable.chat_message_background_you)
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

    fun setData(newViewItems: List<Message>) {
        viewItems = newViewItems
        notifyDataSetChanged()
    }
}
