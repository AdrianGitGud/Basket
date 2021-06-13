package com.example.basket.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.R
import com.example.basket.models.ShoppingListModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ShoppingAdapter(
        options: FirestoreRecyclerOptions<ShoppingListModel>,
        private val listener: OnItemClickListener
) : FirestoreRecyclerAdapter<ShoppingListModel, ShoppingAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.item_shopping_list,
                        parent, false
                )

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ShoppingListModel) {

        holder.shoppingListNameTextView.text = model.shoppingListName
        holder.createdByTextView.text = model.createdBy
        val dateFormat: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK)
        holder.dateTextView.text = "Creada: ${dateFormat.format(model.date)}"
        holder.idTextView.text = model.shoppingListId
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
        notifyDataSetChanged()
    }

    fun editItem(position: Int, map: MutableMap<String, Any>){
        snapshots.getSnapshot(position).reference.update(map)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{
        val shoppingListNameTextView: TextView = itemView.findViewById(R.id.shopping_list_name_text_view)
        val createdByTextView:TextView = itemView.findViewById(R.id.created_by_text_view)
        val dateTextView:TextView = itemView.findViewById(R.id.date_text_view)
        val idTextView: TextView = itemView.findViewById(R.id.id_text_view)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)

        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, idTextView.text, shoppingListNameTextView.text, createdByTextView.text)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemHold(position, idTextView.text, shoppingListNameTextView.text, createdByTextView.text)
            }
            return true
        }
    }

    interface OnItemClickListener {

        fun onItemClick(position: Int, id: CharSequence, shoppingListNameTextView: CharSequence, createdByTextView: CharSequence)
        fun onItemHold(position: Int, id: CharSequence, shoppingListNameTextView: CharSequence, createdByTextView: CharSequence)
    }
}
