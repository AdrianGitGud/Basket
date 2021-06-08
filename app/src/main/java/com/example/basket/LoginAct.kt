package com.example.basket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.basket.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.installations.FirebaseInstallations

class LoginAct : AppCompatActivity() {
    // Value default de google
    private val GOOGLE_SIGN_IN = 100

    // Callback Manager de Facebook
    private val callbackManager = CallbackManager.Factory.create()
    // Binding
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Splash
        Thread.sleep(2000)
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        // Setup
        notificacion()
        setup()
        session()
    }

    private fun notificacion(){
        FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
            it.result?.token?.let {
                println("Este el token del dispositivo ${it}")
            }
        }

        // Recuperar información

        val url = intent.getStringExtra("url")
        url?.let {
            println("Ha llegado información en una push: ${it}")
        }

        // Temas
        //FirebaseMessaging.getInstance().subscribeToTopic("Tema")
    }

    override fun onStart() {
        super.onStart()

        binding.authLayout.visibility = View.VISIBLE
    }

    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        // Comprobamos si ya existe una sesión activa. Si es así, nos saltamos el login.
        if (email != null && provider != null) {
            binding.authLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title = "Autenticación"
        binding.RegisterButton.setOnClickListener{
            if (binding.EmailEditText.text.isNotEmpty() && binding.PasswordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(binding.EmailEditText.text.toString(),
                                binding.PasswordEditText.text.toString()).addOnCompleteListener {

                            if (it.isSuccessful){

                                showHome(it.result?.user?.email ?: "", ProviderType.BASICO)
                            } else {
                                showAlert()
                            }
                        }
            }
        }
        // Boton Login Email
        binding.LoginButton.setOnClickListener{
            if (binding.EmailEditText.text.isNotEmpty() && binding.PasswordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(binding.EmailEditText.text.toString(),
                                binding.PasswordEditText.text.toString()).addOnCompleteListener {

                            if (it.isSuccessful){
                                showHome(it.result?.user?.email ?: "", ProviderType.BASICO)
                            } else {
                                showAlert()
                            }
                        }
            }
        }
        // Boton Login Google
        binding.googleButton.setOnClickListener {

            // Configuracion
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }
        // Boton Login Facebook
        binding.facebookButton.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))

            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult>{

                    override fun onSuccess(result: LoginResult?) {

                        result?.let {

                            val token = it.accessToken

                            val credential = FacebookAuthProvider.getCredential(token.token)
                            //Registramos la autenticación en Firebase
                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                                if (it.isSuccessful){
                                    showHome(it.result?.user?.email ?: "", ProviderType.FACEBOOK)

                                } else {
                                    showAlert()
                                }
                            }
                        }
                    }

                    override fun onCancel() {
                        TODO("Not yet implemented")
                    }

                    override fun onError(error: FacebookException?) {
                        showAlert()
                    }

                })

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

    /*private fun getName() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Cómo deberíamos llamarte?")

        val input = EditText(this)

        input.hint = "Introduce tu nombre"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setCancelable(false).setPositiveButton("Aceptar"){ dialogInterface, i ->
            val userName = input.text.toString()
        }.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)

                //Comprobamos si la cuenta existe
                if (account != null) {

                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    //Registramos la autenticación en Firebase
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful){
                            showHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }

                }
            } catch (e: ApiException){
                showAlert()
            }



        }
    }
}