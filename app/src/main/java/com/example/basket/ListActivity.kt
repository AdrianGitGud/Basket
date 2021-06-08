package com.example.basket

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityListBinding


class ListActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        binding.fabAddlist.setOnClickListener {
          startActivity(Intent(this, CreateListActivity::class.java))
        }

    }
}