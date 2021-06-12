package com.example.basket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityCreateListBinding
import com.example.basket.models.ShoppingListModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class CreateListActivity : AppCompatActivity() {
    private var userShoppingListsRef: CollectionReference? = null

    // Binding
    private var isCheckedToggleButton: Boolean = false
    private var isEmpty: Boolean = false
    private lateinit var binding: ActivityCreateListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding

        binding = ActivityCreateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        super.onCreate(savedInstanceState)

        // Inicializaciones
        userShoppingList()

        binding.siguienteButton.setOnClickListener {
            val msg: String = binding.textInputEditText.text.toString()
            if (msg.trim().isNotEmpty()) {

                val shoppingListName = binding.textInputEditText.text.toString()
                addShoppingList(shoppingListName)
                onBackPressed()

            } else {

                binding.textInputEditText.error = "Debes ponerle un nombre a la lista"

            }
        }

        binding.closeButton.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
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

    private class ToggleButtonsGroup : CompoundButton.OnCheckedChangeListener {
        private var mButtons: ArrayList<ToggleButton> = ArrayList()

        fun addButton(btn: ToggleButton) {
            btn.setOnCheckedChangeListener(this)
            mButtons.add(btn)
        }

        fun ToggleButtonsGroup() {
            mButtons = ArrayList()
        }

        val selectedButton: ToggleButton?
            get() {
                for (b in mButtons) {
                    if (b.isChecked) return b
                }
                return null
            }

        override fun onCheckedChanged(buttonView: CompoundButton,
                                      isChecked: Boolean) {
            if (isChecked) {
                uncheckOtherButtons(buttonView.id)
                var mDataChanged = true
            } else if (!anyButtonChecked()) {
                buttonView.isChecked = true
            }
        }

        private fun anyButtonChecked(): Boolean {
            for (b in mButtons) {
                if (b.isChecked) return true
            }
            return false
        }

        private fun uncheckOtherButtons(current_button_id: Int) {
            for (b in mButtons) {
                if (b.id != current_button_id) b.isChecked = false
            }
        }

    }
}
