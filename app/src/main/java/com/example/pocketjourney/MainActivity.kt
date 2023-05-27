package com.example.pocketjourney

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pocketjourney.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            binding.etEmailLogin.setText("suca")
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
}
