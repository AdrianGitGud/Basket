package com.example.basket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basket.databinding.ActivityHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType{
    BASICO,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    // Binding
    private lateinit var binding:ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        //Setup
        val bundle =  intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")



        // Guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()



        binding.profileButton.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("provider", provider)
            }
            startActivity(profileIntent)
            finish()
        }

        binding.listButton.setOnClickListener {
            val listIntent = Intent(this, ListActivity::class.java).apply {
                putExtra("email", email)
                putExtra("provider", provider)
            }
            startActivity(listIntent)
            finish()
        }
    }

    private fun setup(){

        title = "Inicio"


    }
}