package com.example.pocketjourney.accesso

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DbHelper
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.ActivityLoginBinding
import com.example.pocketjourney.home.HomeActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var context:Context

    companion object {
        const val PREFS_NAME = "MyPrefs"
        const val KEY_IS_LOGGED_IN = "isLoggedIn"
        const val KEY_IDUTENTE = "idUtente"
        const val KEY_EMAIL ="email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context=this

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        if (isLoggedIn){
            Log.e("ciao","l'utente è loggato")
            val idUtente = sharedPreferences.getString(KEY_IDUTENTE, null)
            val email = sharedPreferences.getString(KEY_EMAIL, null)
            if (idUtente != null && email != null) {
                val intent = Intent(context, HomeActivity::class.java)
                intent.putExtra("idUtente", idUtente)
                intent.putExtra("email", email)
                startActivity(intent)
            }
        }


        binding.btnLogin.setOnClickListener{
            //provo a effettuare il login con le credenzili online
            val email = binding.etEmailLogin.text.toString()
            val password=binding.etPasswordLogin.text.toString()
            val userAPI= ClientNetwork.retrofit
            val queryLogin = "SELECT *  FROM Utente WHERE email = '$email' AND password = '$password'"
            val call = userAPI.cerca(queryLogin)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        Log.d("JSON", response.body().toString())
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {
                            Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset login" )
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                val primoUtente=querySetArray[0].asJsonObject //prendo la prima corrispondenza
                                //verifico che non sia null e che contenga i campi corretti
                                if (primoUtente!=null && primoUtente.has("idUtente") ){
                                    //prelevo i campi e li setto nel fragment
                                    val idUtente=primoUtente.get("idUtente").asString
                                    //posso effettuare il login online
                                    val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(KEY_IS_LOGGED_IN, true)
                                    editor.putString(KEY_IDUTENTE,idUtente)
                                    editor.putString(KEY_EMAIL,email)
                                    editor.apply()
                                    Log.e("HO SCRITTO LE SP", editor.toString())
                                    val intent = Intent(context, HomeActivity::class.java)
                                    intent.putExtra("idUtente",idUtente)
                                    intent.putExtra("emailOnline",email)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }


                    } else {
                        //credenziali errate
                        Toast.makeText(context,"Credenziali non valide",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    //login in locale
                    if(verificaCredenzialiLocale(email,password)) {
                        Log.e("ciao","LOGIN OFFLINE")
                        val intent = Intent(context, HomeActivity::class.java)
                        intent.putExtra("email",email)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("ciao", "credenziali offline errate")
                        Toast.makeText(context,"Credenziali non valide",Toast.LENGTH_SHORT).show()
                    }
                    Log.e("ciao", t.toString() + " " + t.message.toString())
                }
            })

        }


        binding.testHomeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        binding.btnCreaAccount.setOnClickListener {
            val registrationFragment= RegistrationFragment()
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerRegistration, registrationFragment)
                .addToBackStack(null)
                .commit()
            binding.btnLogin.visibility=View.GONE
            binding.btnCreaAccount.visibility=View.GONE
        }
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    fun verificaCredenzialiLocale(email:String,password:String):Boolean{
        val dbHelper= DbHelper(this)

        val db=dbHelper.readableDatabase
        val proiezione=arrayOf("email","password")
        val selezione= "email=?"
        val selectionArgs= arrayOf(email)
        val cursor=db.query("Utenti",proiezione,selezione,selectionArgs,null,null,null)
        if (cursor.moveToFirst()) {
            val emailSalvata = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val passwordSalvata = cursor.getString(cursor.getColumnIndexOrThrow("password"))

            if (email == emailSalvata && password == passwordSalvata) {
                cursor.close()
                db.close()
                return true
            }
        }

        cursor.close()
        db.close()
        return false
    }

    override fun onPause() {
        super.onPause()
        binding.etEmailLogin.text.clear()
        binding.etPasswordLogin.text.clear()
    }

}