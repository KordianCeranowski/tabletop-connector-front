package com.example.tabletop.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tabletop.R
import com.example.tabletop.mvvm.model.Game
import kotlinx.android.synthetic.main.row_game.view.*

class GameAdapter : RecyclerView.Adapter<GameAdapter.MyViewHolder>() {

    private var viewItems = emptyList<Game>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(game: Game) {
            itemView.apply {
                row_game_name.text = game.name
                //row_game_image.setImageURI(Uri.parse(game.image))
                row_game_players.text = run {
                    StringBuilder()
                        .append("Players: ")
                        .append(game.minPlayers)
                        .append('-')
                        .append(game.maxPlayers)
                        .toString()
                }
                row_game_play_time.text = run {
                    StringBuilder()
                        .append(game.playTime)
                        .append(" min")
                        .toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_game, parent, false)
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
}