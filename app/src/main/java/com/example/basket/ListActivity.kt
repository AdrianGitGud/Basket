package com.example.basket

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

        val bundle =  intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        binding.fabAddlist.setOnClickListener {
            val profileIntent = Intent(this, CreateListActivity::class.java).apply {
                putExtra("provider", provider)
            }
            startActivity(profileIntent)
        }

    }
}