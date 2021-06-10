package com.example.basket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basket.databinding.ActivityListBinding
import com.example.basket.holders.ShoppingAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ListActivity : AppCompatActivity() {

    var shoppingAdapter: ShoppingAdapter? = null
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


    private fun setUpRecyclerView() {

        val bundle =  intent.extras
        val userEmail = bundle?.getString("email")


        val collectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("shoppingLists").document(userEmail.toString()).collection("userShoppingLists")

        val query:Query = collectionReference

        val options: FirestoreRecyclerOptions<Lists> = FirestoreRecyclerOptions.Builder<Lists>()
                .setQuery(query, Lists::class.java)
                .build()

        shoppingAdapter = ShoppingAdapter(options)

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