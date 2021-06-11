package com.example.basket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basket.databinding.ActivityShoppingListBinding
import com.example.basket.models.ShoppingListModel


class ShoppingListActivity : AppCompatActivity() {
    // Binding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityShoppingListBinding
    private lateinit var id:String
    private lateinit var name:String
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


        var shoppingListModel = intent.getSerializableExtra("shoppingListModel") as ShoppingListModel?
        val shoppingListName: String? = shoppingListModel?.shoppingListName

        binding.recetasTextView.text = name


    }
}