package com.example.pianotiles

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class LevelAdapter(private val levels: List<Level>) :
    RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    class LevelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.levelNumber)
        val description: TextView = view.findViewById(R.id.levelDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.level_item, parent, false)
        return LevelViewHolder(view)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val level = levels[position]
        holder.number.text = level.number.toString()
        holder.description.text = level.description

        // simple click listener
        holder.itemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Level ${level.number} clicked",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(holder.itemView.context, MenuActivity::class.java)
            intent.putExtra("SELECTED_LEVEL", level.number)
            (holder.itemView.context as Activity).setResult(Activity.RESULT_OK, intent)
            (holder.itemView.context as Activity).finish()
        }
    }

    override fun getItemCount(): Int = levels.size
}
