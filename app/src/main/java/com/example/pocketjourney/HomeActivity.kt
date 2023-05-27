package com.example.pocketjourney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.pocketjourney.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.foodButton.setOnClickListener{
                //debug
            binding.textView3.setText("food")
        }

        binding.hotelButton.setOnClickListener{

            binding.textView3.setText("Hotel")
        }

        binding.locationButton.setOnClickListener{

            binding.textView3.setText("Location")
        }

        binding.searchButton.setOnClickListener{
            val searchText: EditText = findViewById(R.id.searchText)
            val textToSearch: String = searchText.text.toString()

            if(textToSearch.isNotEmpty()){
                binding.textView3.setText(textToSearch)
                //TODO: query al dbms

            }else{
                binding.searchText.setHint("Se non mi dici cosa cercare come posso trovarlo?")
            }

        }


    }



}