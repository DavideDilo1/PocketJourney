package com.example.pocketjourney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.pocketjourney.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        //questo valore bundle ottiene l'id dell'utente nel caso in cui sia connesso al server, l'email viceversa.
        val bundle=intent.extras
        val emailOff= bundle?.getString("emailutenteOffline")
        val userId= bundle?.getString("idUtente")
        if(userId!=null){
            Log.e("ciao","sono la home activity e ho ricevuto l'id utente")
        } else {
            Log.e("ciao","sono la home activity e NON ho ricevuto id Utente")
        }
        if(emailOff!=null){
            Log.e("ciao","sono la home activity e ho ricevuto emailOff ")
        } else {
            Log.e("ciao","sono la home activity e NON  ho ricevuto emailOff ")
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(HomeFragmentNew())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragmentNew())
                R.id.preferiti -> replaceFragment(PreferitiFragment())
                //passo al fragment profilo l'id dell'utente nel caso sia connesso, l'email altrimenti
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
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun replaceFragmentProfile(fragment: Fragment,bundle:Bundle){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frameHomeLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }



}