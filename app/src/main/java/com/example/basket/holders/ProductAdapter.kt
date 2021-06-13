package com.example.basket.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.R
import com.example.basket.models.ProductModel
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ProductAdapter(
        options: FirestoreRecyclerOptions<ProductModel>,
) : FirestoreRecyclerAdapter<ProductModel, ProductAdapter.MyViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context)
                .inflate(
                        R.layout.item_product,
                        parent, false
                )

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ProductModel) {

        holder.productName.text = model.productName

    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
        notifyDataSetChanged()
    }

    fun editItem(position: Int, map: MutableMap<String, Any>){
        snapshots.getSnapshot(position).reference.update(map)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productName: TextView = itemView.findViewById(R.id.productTextView)

    }
}
