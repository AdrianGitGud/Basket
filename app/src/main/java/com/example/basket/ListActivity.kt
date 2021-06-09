package com.example.basket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basket.databinding.ActivityListBinding
import com.example.basket.models.ShoppingListModel
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ListActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        val bundle =  intent.extras
        val userEmail = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        binding.fabAddlist.setOnClickListener {
            val profileIntent = Intent(this, CreateListActivity::class.java).apply {
                putExtra("provider", provider)
            }
            startActivity(profileIntent)
        }
        var userShoppingListsRef = FirebaseFirestore.getInstance().collection("shoppingLists").document(
            userEmail.toString()
        ).collection("userShoppingLists")

        var recyclerView = binding.listsReciclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        var query:Query = userShoppingListsRef.orderBy(
            "shoppingListsName",
            Query.Direction.ASCENDING
        )

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<ShoppingListModel>()
            .setQuery(query, ShoppingListModel::class.java)
            .build()
    }
}