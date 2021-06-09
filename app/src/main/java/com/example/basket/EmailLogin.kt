package com.example.basket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.basket.databinding.ActivityEmailLoginBinding
import com.example.basket.models.UserModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations

class EmailLogin : AppCompatActivity() {
    // Binding
    var tokenId = ""
    private lateinit var binding: ActivityEmailLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar?.show()
        setup()

    }

    private fun notificacion() {
        FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
            it.result?.token?.let {
                println("Este el token del dispositivo ${it}")
                tokenId = it
            }
        }
    }

    private fun setup() {
        title = "Empecemos"
        binding.RegisterButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            val userEmail: String = it.result?.user?.email ?: ""
                            val userModel = UserModel(userEmail, tokenId)
                            FirebaseFirestore.getInstance().collection("users").document(userEmail)
                                .set(userModel).addOnSuccessListener {
                                    Log.d(
                                        "TAG",
                                        "User successfully created!"
                                    )
                                }
                            showHome(userEmail, ProviderType.BASICO)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
        // Boton Login Email
        binding.LoginButton.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            val userEmail: String = it.result?.user?.email ?: ""
                            val userModel = UserModel(userEmail, tokenId)
                            FirebaseFirestore.getInstance().collection("users").document(userEmail)
                                .set(userModel).addOnSuccessListener {
                                    Log.d(
                                        "TAG",
                                        "User successfully created!"
                                    )
                                }
                            showHome(userEmail, ProviderType.BASICO)
                        } else {
                            showAlert()
                        }
                    }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply{
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
}