package com.example.basket

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ListActivity : AppCompatActivity(), ShoppingAdapter.OnItemClickListener {
    private lateinit var friendEmail: String
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
        }

        setUpRecyclerView()
    }

    override fun onItemClick(position: Int, id: CharSequence, shoppingListNameTextView: CharSequence, createdByTextView: CharSequence) {
        val productsIntent = Intent(this, ShoppingListActivity::class.java).apply {
            putExtra("Name", shoppingListNameTextView)
            putExtra("Id", id)
            putExtra("Owner", createdByTextView)
        }
        startActivity(productsIntent)
    }

    override fun onItemHold(position: Int, id: CharSequence, shoppingListNameTextView: CharSequence, createdByTextView: CharSequence) {

        val bundle =  intent.extras
        val userEmail = bundle?.getString("email")
        val shoppingListModel = ShoppingListModel(id.toString(), shoppingListNameTextView.toString(), userEmail)

        val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        builder.setTitle("Comparte esta lista")
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
        editText.setSelection(editText.text.length)
        editText.hint = "Escribe el email de tu amigo"
        editText.setHintTextColor(Color.GRAY)
        builder.setView(editText)
        val rootRef = FirebaseFirestore.getInstance()
        builder.setPositiveButton("Aceptar") { dialogInterface, i ->
            friendEmail = editText.text.toString().toLowerCase().trim { it <= ' ' }
            rootRef.collection("shoppingLists").document(friendEmail)
                    .collection("userShoppingLists").document(id as String)
                    .set(shoppingListModel).addOnSuccessListener {
                        val users: HashMap<String, Any> = HashMap()
                        val map: MutableMap<String, Any> = java.util.HashMap()
                        map[userEmail!!] = true
                        map[friendEmail] = true
                        users["users"] = map
                        rootRef.collection("shoppingLists").document(userEmail)
                                .collection("userShoppingLists").document(id)
                                .update(users)
                        rootRef.collection("shoppingLists").document(friendEmail)
                                .collection("userShoppingLists").document(id)
                                .update(users)
                    Toast.makeText(this,"Lista compartida con $friendEmail", Toast.LENGTH_SHORT).show()
                    }
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

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@ListActivity, R.color.colorTertiary))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate()
            }

        }).attachToRecyclerView(recyclerView)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(this@ListActivity, R.style.MyDialogTheme)
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
                    if (newShoppingListName.isBlank()) {
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
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(this@ListActivity, R.color.colorQuaternary))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_edit_24)
                        .create()
                        .decorate()

            }

        }).attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        shoppingAdapter!!.startListening()
    }

    override fun onResume() {
        super.onResume()
        shoppingAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        shoppingAdapter!!.stopListening()
    }

}