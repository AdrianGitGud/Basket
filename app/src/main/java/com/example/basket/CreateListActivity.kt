package com.example.basket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.basket.databinding.ActivityCreateListBinding
import com.example.basket.databinding.ActivityListBinding

class CreateListActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityCreateListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityCreateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)


        binding.siguienteButton.setOnClickListener {

            if (binding.textInputEditText.text.toString().trim().isEmpty()){

                binding.textInputEditText.error = "Debes ponerle un nombre a la lista"

            } else{

                val shoppingListName =
                    binding.textInputEditText
                        .toString()
                        .trim { it <= ' ' }
                //addShoppingList(shoppingListName)

            }


        }

        binding.closeButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }
}