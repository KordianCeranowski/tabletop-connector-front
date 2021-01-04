package com.example.tabletop.main.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.mvvm.model.Game
import com.example.tabletop.util.setImageFromURL
import kotlinx.android.synthetic.main.row_chosen_game.view.*
import kotlinx.android.synthetic.main.row_game.view.*
import kotlinx.android.synthetic.main.row_game.view.row_game_name
import net.alexandroid.utils.mylogkt.logI
import splitties.toast.toast

class ChosenGameAdapter : RecyclerView.Adapter<ChosenGameAdapter.MyViewHolder>() {

    private var viewItems = emptyList<Game>()
    var activity: Activity? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            itemView.apply {
                row_game_name.text = game.name
                row_chosen_game_image.setImageFromURL(context, game.thumbnail)
                button.setOnClickListener {
                    viewItems = viewItems - game
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_chosen_game, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        val game = viewItems[position]
        viewHolder.bind(game)
    }

    override fun getItemCount(): Int {
        return viewItems.size
    }

    fun setData(newList: List<Game>) {
        viewItems = newList
        notifyDataSetChanged()
    }

    fun addGame(game: Game){
        viewItems = viewItems + game
        notifyDataSetChanged()
    }
}