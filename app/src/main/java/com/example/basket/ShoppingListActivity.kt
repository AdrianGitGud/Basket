package com.example.basket

import android.content.Context
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
import com.example.basket.databinding.ActivityShoppingListBinding
import com.example.basket.holders.ProductAdapter
import com.example.basket.models.ProductModel
import com.example.basket.utils.SpacingItemDecorator
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class ShoppingListActivity : AppCompatActivity() {
    // Binding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityShoppingListBinding
    var productAdapter: ProductAdapter?= null
    private lateinit var id:String
    private lateinit var name:String
    private lateinit var owner:String
    private var productsRef: CollectionReference? = null
    private var friendProductsRef: CollectionReference? = null
    private var ownerListRef: CollectionReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        linearLayoutManager = LinearLayoutManager(this)
        var recyclerView = binding.itemsRecyclerView
        recyclerView.layoutManager = linearLayoutManager

        val objectIntent: Intent = intent

        id = objectIntent.getStringExtra("Id").toString()
        name = objectIntent.getStringExtra("Name").toString()
        owner = objectIntent.getStringExtra("Owner").toString()
        userProducts()

        binding.recetasTextView.text = name

        setUpRecyclerView()

        binding.fabAddproduct.setOnClickListener {

            val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
            builder.setTitle("A??ade un producto")
            val editText = EditText(this)
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            editText.setSelection(editText.text.length)
            editText.hint = "Escribe un articulo"
            editText.setHintTextColor(Color.GRAY)
            builder.setView(editText)
            builder.setPositiveButton("A??adir") { dialogInterface, i ->
                val productName = editText.text.toString().trim { it <= ' ' }
                addProduct(productName)
            }
            builder.setNegativeButton("Cancelar") { dialogInterface, i -> dialogInterface.dismiss() }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

    }

    private fun userProducts(){

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)

        productsRef = FirebaseFirestore.getInstance()
                .collection("products")
                .document(userEmail.toString()).collection("userProducts")

        friendProductsRef = FirebaseFirestore.getInstance()
                .collection("products")
                .document(owner).collection("userProducts")
        ownerListRef = if (owner != userEmail) {
            FirebaseFirestore.getInstance()
                    .collection("shoppingLists")
                    .document(owner)
                    .collection("userShoppingLists")
        }else{
            FirebaseFirestore.getInstance().collection("shoppingLists").document(userEmail.toString()).collection("userShoppingLists")
        }
    }

    private fun addProduct(productName: String) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)
        val productId = productsRef?.document()?.id
        val productModel = ProductModel(productId, productName, id)
        if (productId != null) {
            productsRef?.document(productId)
                    ?.set(productModel)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "$productName a??adido a la lista", Toast.LENGTH_SHORT).show()
                    }
            if (owner != userEmail){
                friendProductsRef?.document(productId)
                        ?.set(productModel)
            }
        }
    }

    private fun deleteProduct(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)
        val productId = productsRef?.document()?.id

        if (productId != null) {
            productsRef?.document(productId)
                    ?.delete()
            if (owner != userEmail){
                friendProductsRef?.document(productId)
                        ?.delete()
            }
        }

    }

    private fun setUpRecyclerView() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)

        val collectionReference: CollectionReference = FirebaseFirestore.getInstance()
                    .collection("products")
                    .document(userEmail.toString()).collection("userProducts")


        val query: Query = collectionReference.whereEqualTo("shoppingListId", id)

        val options: FirestoreRecyclerOptions<ProductModel> = FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel::class.java).setLifecycleOwner(this).build()

        productAdapter = ProductAdapter(options)

        val recyclerView = binding.itemsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val decorator: SpacingItemDecorator = SpacingItemDecorator(20)
        recyclerView.addItemDecoration(decorator)
        recyclerView.adapter = productAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                productAdapter!!.deleteItem(viewHolder.adapterPosition)
            }

           override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@ShoppingListActivity, R.color.colorTertiary))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate()
            }

        }).attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        productAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        productAdapter!!.stopListening()
    }
}


