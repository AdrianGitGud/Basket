package com.example.basket.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.Lists
import com.example.basket.R

class ShoppingListViewHolder(private val shoppingList: ArrayList<Lists>) : RecyclerView.Adapter<ShoppingListViewHolder.MyViewHolder>(){




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_shopping_list,
                        parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = shoppingList[position]
        holder.shoppingListNameTextView.text = currentItem.tituloLista
        holder.createdByTextView.text = currentItem.createdBy
        holder.dateTextView.text = currentItem.fechaCreado

    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val shoppingListNameTextView: TextView = itemView.findViewById(R.id.shopping_list_name_text_view)
        val createdByTextView:TextView = itemView.findViewById(R.id.created_by_text_view)
        val dateTextView:TextView = itemView.findViewById(R.id.date_text_view)
    }
}