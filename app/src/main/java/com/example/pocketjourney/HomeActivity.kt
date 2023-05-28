package com.example.pocketjourney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.pocketjourney.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

 /*

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

        }*/

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.preferiti -> replaceFragment(PreferitiFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                else ->{

                }

            }

            true
        }

    }


    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()


    }



}