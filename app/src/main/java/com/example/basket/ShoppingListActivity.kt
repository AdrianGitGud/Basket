package com.example.basket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityShoppingListBinding
import com.example.basket.models.ShoppingListModel


class ShoppingListActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityShoppingListBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)



        var shoppingListModel = intent.getSerializableExtra("shoppingListModel") as ShoppingListModel?
        val shoppingListName: String? = shoppingListModel?.shoppingListName

        binding.tituloTextView.text = shoppingListName


    }
}