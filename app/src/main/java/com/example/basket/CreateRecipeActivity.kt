package com.example.basket

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.basket.databinding.ActivityCreateRecipeBinding
import com.example.basket.models.RecipeModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CreateRecipeActivity : AppCompatActivity() {

    private var userRecipesRef: CollectionReference? = null
    var imageUri: Uri? = null
    var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    var imagesRef: StorageReference? = storageRef.child("images")
    private lateinit var binding: ActivityCreateRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityCreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        userRecipes()
        //uploadPhoto()

        binding.crearRecetaImagen.setOnClickListener {



        }

        binding.closeButton.setOnClickListener {
            onBackPressed()
            finish()
        }

        binding.siguienteButton.setOnClickListener {
            val msg: String = binding.crearRecetaNombre.text.toString()
            val msg2: String = binding.crearRecipeDescripcion.text.toString()
            val msg3: String = binding.crearRecipeIngredientes.text.toString()
            if (msg.trim().isNotEmpty() && msg2.trim().isNotEmpty() && msg3.trim().isNotEmpty()) {

                val recipeName = binding.crearRecetaNombre.text.toString()
                val recipeDesc = binding.crearRecipeDescripcion.text.toString()
                val recipeIngredientes = binding.crearRecipeIngredientes.text.toString()
                addRecipe(recipeDesc, recipeIngredientes, recipeName)
                //upLoadToFirebase(imageUri!!)
                startActivity(Intent(this, RecipesActivity::class.java))

            } else {

                Toast.makeText(this, "Rellena todos los campos porfavor", Toast.LENGTH_SHORT).show()

            }
        }
    }

    /*private fun upLoadToFirebase(uri: Uri){

        var fileRef: StorageReference = storageRef.child("${System.currentTimeMillis()}.${getFileExt(uri)}")
        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                Toast.makeText(this, "Imagen subida con exito", Toast.LENGTH_SHORT).show()
                val recipeId =  userRecipesRef?.document()?.id
                val recipeModel = RecipeModel(recipeId, recipeName, recipeDesc, recipeIngredientes)
                if (recipeId != null) {
                    userRecipesRef?.document(recipeId)
                        ?.set(recipeModel)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Fallo al subir la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExt(mUri: Uri): String? {

        val cr: ContentResolver? = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        if (cr != null) {
            return mime.getExtensionFromMimeType(cr.getType(mUri))
        }
    }

    private fun uploadPhoto(){

        var photoIntent = Intent()
        photoIntent.type = "image/*"
        photoIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(photoIntent, 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && requestCode == RESULT_OK && data != null){

            imageUri = data.data
            binding.crearRecetaImagen.setImageURI(imageUri)

        }
    }
*/*/
    private fun userRecipes(){

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userEmail = prefs.getString("email", null)

        userRecipesRef = FirebaseFirestore.getInstance()
            .collection("recipes")
            .document(userEmail.toString()).collection("userRecipes")
    }

    private fun addRecipe(recipeDesc: String, recipeIngredientes: String, recipeName: String) {

        val recipeId =  userRecipesRef?.document()?.id
        val recipeModel = RecipeModel(recipeId, recipeName, recipeDesc, recipeIngredientes)
        if (recipeId != null) {
            userRecipesRef?.document(recipeId)
                ?.set(recipeModel)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "La receta $recipeName ha sido creada", Toast.LENGTH_SHORT).show()
                }
        }
    }
}