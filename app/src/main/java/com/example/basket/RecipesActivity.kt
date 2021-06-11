package com.example.basket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityRecipesBinding

class RecipesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}