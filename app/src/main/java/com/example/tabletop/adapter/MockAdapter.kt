package com.example.tabletop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.model.Post
import kotlinx.android.synthetic.main.row_mock_post.view.*

class MockAdapter : RecyclerView.Adapter<MockAdapter.MyViewHolder>() {

    private var viewItems = emptyList<Post>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_mock_post, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.userId_txt.text = viewItems[position].userId.toString()
        holder.itemView.id_txt.text = viewItems[position].id.toString()
        holder.itemView.title_txt.text = viewItems[position].title
        holder.itemView.body_txt.text = viewItems[position].body
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<Post>) {
        viewItems = newList
        notifyDataSetChanged()
    }
}