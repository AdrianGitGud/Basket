package com.example.basket

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityCreateListBinding
import com.example.basket.models.ShoppingListModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CreateListActivity : AppCompatActivity() {
    private var userShoppingListsRef: CollectionReference? = null
    val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
    val userName = prefs.getString("name", null)
    val userEmail = prefs.getString("email", null)

    // Binding
    private lateinit var binding: ActivityCreateListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityCreateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        // Conseguir el nombre




        binding.siguienteButton.setOnClickListener {

            if (binding.textInputEditText.text.toString().trim().isEmpty()) {

                binding.textInputEditText.error = "Debes ponerle un nombre a la lista"

            } else {

                val shoppingListName =
                        binding.textInputEditText
                                .toString()
                                .trim { it <= ' ' }
                addShoppingList(shoppingListName)

            }
        }

        binding.closeButton.setOnClickListener {
            onBackPressed()
            finish()
        }

        userShoppingListsRef = FirebaseFirestore.getInstance()
                .collection("shoppingLists")
                .document(userEmail.toString()).collection("userShoppingLists")



    }

    private fun addShoppingList(shoppingListName: String) {

        var shoppingListId = userShoppingListsRef?.document()?.id
        val shoppingListModel = ShoppingListModel(shoppingListId, shoppingListName, userEmail)
        if (shoppingListId != null) {
            userShoppingListsRef?.document(shoppingListId)
                    ?.set(shoppingListModel)
                    ?.addOnSuccessListener {
                        Toast.makeText(this, "Lista $shoppingListName creada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
