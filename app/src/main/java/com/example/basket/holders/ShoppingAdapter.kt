package com.example.basket.holders

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.R
import com.example.basket.ShoppingListActivity
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


        /*itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(parent.context)
            builder.setTitle("Edit Shopping List Name")
            val editText = EditText(parent.context)
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            editText.setText(shoppingListName)
            editText.setSelection(editText.text.length)
            editText.hint = "Type a name"
            editText.setHintTextColor(Color.GRAY)
            builder.setView(editText)
            val rootRef = FirebaseFirestore.getInstance()
            val map: MutableMap<String, Any> = HashMap()
            builder.setPositiveButton("Actualizar") { dialogInterface, i ->
                val newShoppingListName = editText.text.toString().trim { it <= ' ' }
                map["shoppingListName"] = newShoppingListName
                rootRef.collection("shoppingLists").document(userEmail!!).collection("userShoppingLists").document(shoppingListId).update(map)
            }
            builder.setNegativeButton("Cancelar") { dialogInterface, i -> dialogInterface.dismiss() }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            true
        }*/

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int, model: ShoppingListModel) {

        holder.shoppingListNameTextView.text = model.shoppingListName
        holder.createdByTextView.text = model.createdBy
        val dateFormat: DateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK)
        val shoppingListCreationDate: String = dateFormat.format(model.date)
        holder.dateTextView.text = "Creada: $shoppingListCreationDate"
    }



    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val shoppingListNameTextView: TextView = itemView.findViewById(R.id.shopping_list_name_text_view)
        val createdByTextView:TextView = itemView.findViewById(R.id.created_by_text_view)
        val dateTextView:TextView = itemView.findViewById(R.id.date_text_view)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}



    /*fun setShoppingList(context: Context, userEmail: String?, shoppingListModel: ShoppingListModel, itemView: View): MyViewHolder {
        val shoppingListId: String? = shoppingListModel.shoppingListId
        val shoppingListName: String? = shoppingListModel.shoppingListName
        val shoppingListNameTextView: TextView = itemView.findViewById(R.id.shopping_list_name_text_view)
        val createdByTextView:TextView = itemView.findViewById(R.id.created_by_text_view)
        val dateTextView:TextView = itemView.findViewById(R.id.date_text_view)
        shoppingListNameTextView.text = shoppingListName
        val createdBy = "Creado por: " + shoppingListModel.createdBy
        createdByTextView.text = createdBy
        val date: Date? = shoppingListModel.date
        if (date != null) {
            val dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US)
            val shoppingListCreationDate = dateFormat.format(date)
            dateTextView.text = shoppingListCreationDate
        }
        itemView.setOnClickListener { view ->
            val intent = Intent(view.context, ShoppingListActivity::class.java)
            intent.putExtra("shoppingListModel", shoppingListModel)
            view.context.startActivity(intent)
        }
        itemView.setOnLongClickListener { view ->
            val builder: AlertDialog.Builder = Builder(context)
            builder.setTitle("Edit Shopping List Name")
            val editText = EditText(context)
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            editText.setText(shoppingListName)
            editText.setSelection(editText.text.length)
            editText.hint = "Type a name"
            editText.setHintTextColor(Color.GRAY)
            builder.setView(editText)
            val rootRef = FirebaseFirestore.getInstance()
            val map: MutableMap<String, Any> = HashMap()
            builder.setPositiveButton("Update") { dialogInterface, i ->
                val newShoppingListName = editText.text.toString().trim { it <= ' ' }
                map["shoppingListName"] = newShoppingListName
                rootRef.collection("shoppingLists").document(userEmail!!).collection("userShoppingLists").document(shoppingListId).update(map)
            }
            builder.setNegativeButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
            true
        }
    }*/
