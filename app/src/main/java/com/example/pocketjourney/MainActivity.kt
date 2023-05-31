package com.example.pocketjourney

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pocketjourney.databinding.ActivityMainBinding
import java.net.PasswordAuthentication

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            val email = binding.etEmailLogin.text.toString()
            val password=binding.etPasswordLogin.text.toString()
            if(verificaCredenziali(email,password)) {
                val intent = Intent(this, HomeActivity::class.java)
                val bundle= Bundle()
                bundle.putString("emailutente",email)
                intent.putExtras(bundle)
                startActivity(intent)
            } else {
                Toast.makeText(this,"Credenziali non valide",Toast.LENGTH_SHORT).show()
            }
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

    fun verificaCredenziali(email:String,password:String):Boolean{
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
