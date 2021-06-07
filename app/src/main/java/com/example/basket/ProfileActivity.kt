package com.example.basket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.basket.databinding.ActivityProfileBinding
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    // Binding
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)



        // Info
        info()
        // Logout
        logout()
        //var mdecorView = window.decorView; <-------- Mirar mas adelante

    }

    private fun info() {

        // Titulo de la ventana
        title = "Perfil"

        // Mostrar el email y el provider de la cuenta
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        binding.emailTextView.text = prefs.getString("email", null)
        binding.providerTextView.text = prefs.getString("provider", null)
    }

    private fun logout() {

        binding.logOutButton.setOnClickListener{

            // Borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            //Recogemos el provider
            val bundle =  intent.extras
            val provider = bundle?.getString("provider")

            // Logout Facebook
            if (provider == ProviderType.FACEBOOK.name) {
                LoginManager.getInstance().logOut()
            }

            // Logout Firebase
            FirebaseAuth.getInstance().signOut()

            // Vuelta a la pantalla de login
            val logOutIntent = Intent(this, LoginAct::class.java)
            startActivity(logOutIntent)
            finish()
        }
    }
}