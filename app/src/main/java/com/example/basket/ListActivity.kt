package com.example.basket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basket.databinding.ActivityListBinding
import com.example.basket.holders.ShoppingAdapter
import com.example.basket.models.ShoppingListModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ListActivity : AppCompatActivity(), ShoppingAdapter.OnItemClickListener {

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

        binding.fabAddlist.setOnClickListener {
            val listIntent = Intent(this, CreateListActivity::class.java)
            startActivity(listIntent)
        }

        setUpRecyclerView()
    }

    override fun onItemClick(position: Int) {
        val productsIntent = Intent(this, ShoppingListActivity::class.java).apply {
        }
        startActivity(productsIntent)
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
        recyclerView.adapter = shoppingAdapter

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