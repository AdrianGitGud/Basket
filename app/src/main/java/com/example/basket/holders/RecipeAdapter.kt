package com.example.basket.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.R
import com.example.basket.models.RecipeModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecipeAdapter(
        options: FirestoreRecyclerOptions<RecipeModel>,
) : FirestoreRecyclerAdapter<RecipeModel, RecipeAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.item_recipes,
                        parent, false
                )

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: RecipeModel) {

        holder.recipeTitulo.text = model.recipeName
        holder.recipeDesc.text = model.recipeDesc
        val dateFormat: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK)
        val shoppingListCreationDate: String = dateFormat.format(model.date)
        holder.dateTextView.text = "Creada: $shoppingListCreationDate"
        holder.recipeIngredientes.text = model.recipeIngredientes
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
        notifyDataSetChanged()
    }

    fun editItem(position: Int, map: MutableMap<String, Any>){
        snapshots.getSnapshot(position).reference.update(map)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recipeTitulo: TextView = itemView.findViewById(R.id.recipeTitulo)
        val recipeDesc:TextView = itemView.findViewById(R.id.recipeDescripcion)
        val recipeIngredientes:TextView = itemView.findViewById(R.id.recipeIngredientes)
        val dateTextView: TextView = itemView.findViewById(R.id.recetaFechaCreacion)

    }
}
