package com.example.basket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityCreateListBinding
import com.example.basket.models.ShoppingListModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class CreateListActivity : AppCompatActivity() {
    private var userShoppingListsRef: CollectionReference? = null

    // Binding
    private lateinit var binding: ActivityCreateListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding

        binding = ActivityCreateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        // Inicializaciones
        userShoppingList()


        binding.siguienteButton.setOnClickListener {

            if (binding.textInputEditText.text.toString().trim().isEmpty()) {

                binding.textInputEditText.error = "Debes ponerle un nombre a la lista"

            } else {

                val shoppingListName = binding.textInputEditText.text.toString()
                addShoppingList(shoppingListName)
                startActivity(Intent(this, ListActivity::class.java))
            }
        }

        binding.closeButton.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun userShoppingList(){

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)

        userShoppingListsRef = FirebaseFirestore.getInstance()
                .collection("shoppingLists")
                .document(userEmail.toString()).collection("userShoppingLists")
    }

    private fun addShoppingList(shoppingListName: String) {

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)

        val shoppingListId = userShoppingListsRef?.document()?.id
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
