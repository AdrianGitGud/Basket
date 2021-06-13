package com.example.basket

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.basket.databinding.ActivityRecipesBinding
import com.example.basket.holders.RecipeAdapter
import com.example.basket.models.RecipeModel
import com.example.basket.utils.SpacingItemDecorator
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RecipesActivity : AppCompatActivity() {

    var recipeAdapter: RecipeAdapter? = null
    var options: FirestoreRecyclerOptions<RecipeModel>? = null
    private lateinit var binding: ActivityRecipesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()

        binding.fabAddlist2.setOnClickListener {

         startActivity(Intent(this, CreateRecipeActivity::class.java))

        }
    }

    private fun setUpRecyclerView() {

        val bundle =  intent.extras
        val userEmail = bundle?.getString("email")

        val collectionReference: CollectionReference = FirebaseFirestore.getInstance().collection("recipes").document(userEmail.toString()).collection("userRecipes")

        val query: Query = collectionReference.orderBy("date", Query.Direction.DESCENDING)

        val options: FirestoreRecyclerOptions<RecipeModel> = FirestoreRecyclerOptions.Builder<RecipeModel>()
            .setQuery(query, RecipeModel::class.java)
            .build()

        recipeAdapter = RecipeAdapter(options)

        val recyclerView = binding.recipesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        val decorator: SpacingItemDecorator = SpacingItemDecorator(20)
        recyclerView.addItemDecoration(decorator)
        recyclerView.adapter = recipeAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                recipeAdapter!!.deleteItem(viewHolder.adapterPosition)

            }
        }).attachToRecyclerView(recyclerView)
    }

    override fun onStart() {
        super.onStart()
        recipeAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        recipeAdapter!!.stopListening()
    }
}