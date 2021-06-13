package com.example.basket

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.databinding.ActivityListBinding
import com.example.basket.holders.ShoppingAdapter
import com.example.basket.models.ShoppingListModel
import com.example.basket.utils.SpacingItemDecorator
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ListActivity : AppCompatActivity(), ShoppingAdapter.OnItemClickListener {
    private lateinit var deletedList: String
    var shoppingAdapter: ShoppingAdapter? = null
    val options: FirestoreRecyclerOptions<ShoppingListModel>? = null
            // Binding
    private lateinit var binding: ActivityListBinding
    //var progressBar = binding.progressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        val bundle =  intent.extras
        val email = bundle?.getString("email")

        binding.fabAddlist.setOnClickListener {
            val listIntent = Intent(this, CreateListActivity::class.java)
            startActivity(listIntent)
            finish()
        }

        setUpRecyclerView()
    }

    override fun onItemClick(position: Int, id: CharSequence, shoppingListNameTextView: CharSequence) {
        val productsIntent = Intent(this, ShoppingListActivity::class.java).apply {
            putExtra("Name", shoppingListNameTextView)
            putExtra("Id", id)
        }
        startActivity(productsIntent)
    }

    override fun onItemHold(position: Int, id: CharSequence, shoppingListNameTextView: CharSequence, createdByTextView: CharSequence) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar nombre de la lista")
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        editText.setText(shoppingListNameTextView)
        editText.setSelection(editText.text.length)
        editText.hint = "Escribe un nombre"
        editText.setHintTextColor(Color.GRAY)
        builder.setView(editText)
        val rootRef = FirebaseFirestore.getInstance()
        val map: MutableMap<String, Any> = HashMap()
        builder.setPositiveButton("Actualizar") { dialogInterface, i ->
            val newShoppingListName = editText.text.toString().trim { it <= ' ' }
            map["shoppingListName"] = newShoppingListName
            rootRef.collection("shoppingLists").document(createdByTextView!! as String).collection("userShoppingLists").document(id as String).update(map)
        }
        builder.setNegativeButton("Cancelar") { dialogInterface, i -> dialogInterface.dismiss() }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun setUpRecyclerView() {

        val bundle =  intent.extras
        val userEmail = bundle?.getString("email")

        val collectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("shoppingLists").document(userEmail.toString()).collection("userShoppingLists")

        val query:Query = collectionReference.orderBy("date", Query.Direction.DESCENDING)

        val options: FirestoreRecyclerOptions<ShoppingListModel> = FirestoreRecyclerOptions.Builder<ShoppingListModel>()
                .setQuery(query, ShoppingListModel::class.java)
                .build()

        shoppingAdapter = ShoppingAdapter(options, this)

        val recyclerView = binding.listsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val decorator: SpacingItemDecorator = SpacingItemDecorator(20)
        recyclerView.addItemDecoration(decorator)
        recyclerView.adapter = shoppingAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                shoppingAdapter!!.deleteItem(viewHolder.adapterPosition)

            }
        }).attachToRecyclerView(recyclerView)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(this@ListActivity)
                builder.setTitle("Editar nombre de la lista")
                val editText = EditText(this@ListActivity)
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                editText.setSelection(editText.text.length)
                editText.hint = "Escribe un nombre"
                editText.setHintTextColor(Color.GRAY)
                builder.setView(editText)
                val map: MutableMap<String, Any> = HashMap()
                builder.setPositiveButton("Actualizar") { dialogInterface, i ->
                    val newShoppingListName = editText.text.toString().trim { it <= ' ' }
                    if (newShoppingListName.isBlank()){
                        editText.error = "Debes ingresar un nombre"
                        return@setPositiveButton
                    }
                    map["shoppingListName"] = newShoppingListName
                    shoppingAdapter!!.editItem(viewHolder.adapterPosition, map)
                }
                builder.setNegativeButton("Cancelar") { dialogInterface, i -> dialogInterface.dismiss() }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()
            }

        }).attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        shoppingAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        shoppingAdapter!!.stopListening()
    }

}