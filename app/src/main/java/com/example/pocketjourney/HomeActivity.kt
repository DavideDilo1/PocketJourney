package com.example.pocketjourney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.pocketjourney.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        val bundle=intent.extras


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment())
                R.id.preferiti -> replaceFragment(PreferitiFragment())
                R.id.profile -> bundle?.let { it1 ->
                    replaceFragmentProfile(ProfileFragment(),
                        it1
                    )
                }
                else ->{

                }

            }

            true
        }

    }


    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameHomeLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragmentProfile(fragment: Fragment,bundle:Bundle){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frameHomeLayout, fragment)
        fragmentTransaction.commit()

    }



}