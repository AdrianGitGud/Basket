package com.example.basket

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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
            val createListIntent = Intent(this, CreateListActivity::class.java).apply{
            }
            startActivity(createListIntent)
        }

    }
}