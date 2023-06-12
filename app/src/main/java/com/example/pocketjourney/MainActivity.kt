package com.example.pocketjourney

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.pocketjourney.databinding.ActivityMainBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var context:Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context=this

        binding.btnLogin.setOnClickListener{
            //provo a effettuare il login con le credenzili online
            val email = binding.etEmailLogin.text.toString()
            val password=binding.etPasswordLogin.text.toString()
            val userAPI=ClientNetwork.retrofit
            val queryLogin = "SELECT * FROM Utente WHERE email = '$email' AND password = '$password'"
            val call = userAPI.login(queryLogin)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        //posso effettuare il login online
                        val intent = Intent(context, HomeActivity::class.java)
                        val bundle= Bundle()
                        bundle.putString("emailutente",email)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else {
                        //credenziali errate
                        Toast.makeText(context,"Credenziali non valide",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    //login in locale
                    if(verificaCredenzialiLocale(email,password)) {
                        val intent = Intent(context, HomeActivity::class.java)
                        val bundle= Bundle()
                        bundle.putString("emailutente",email)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    } else {
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
        val dbHelper=DbHelper(this)

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
