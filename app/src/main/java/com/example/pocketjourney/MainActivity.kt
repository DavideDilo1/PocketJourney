package com.example.pocketjourney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pocketjourney.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            //Toast.makeText(this,"Email:"+ binding.etEmailLogin.toString(),Toast.LENGTH_SHORT)
          //  Toast.makeText(this,"Password:"+ binding.etPasswordLogin.toString(),Toast.LENGTH_SHORT)
            binding.etEmailLogin.setText("suca")
        }
    }
}