package com.example.basket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityShoppingListBinding


class ShoppingListActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityShoppingListBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityShoppingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        val bundle =  intent.extras
        val name = bundle?.getString("name")

        binding.tituloTextView.text = "Hola"

    }
}