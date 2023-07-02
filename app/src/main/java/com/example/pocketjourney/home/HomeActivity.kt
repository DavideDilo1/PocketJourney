package com.example.pocketjourney.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.pocketjourney.ProfileFragment
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.ActivityHomeBinding
import com.example.pocketjourney.preferiti.ListaPreferitiFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()
        //questo valore bundle ottiene l'id dell'utente nel caso in cui sia connesso al server, l'email viceversa.
        val emailOff= intent.getStringExtra("email")
        val emailOn= intent.getStringExtra("emailOnline")
        val userId= intent.getStringExtra("idUtente")
        Log.e("SONO H ACT:","${emailOff} ${userId}")

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(HomeFragmentNew(),userId)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragmentNew(), userId)
                R.id.preferiti -> replaceFragment(ListaPreferitiFragment(), userId)
                //passo al fragment profilo l'id dell'utente nel caso sia connesso, l'email altrimenti
                R.id.profile -> replaceFragmentProfile(ProfileFragment(), userId, emailOff)

            }

            true
        }

    }


    private fun replaceFragment(fragment: Fragment, userId: String?){
        val idUtente=userId
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        intent.putExtra("idUtente",idUtente)
        fragmentTransaction.replace(R.id.frameHomeLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        Log.e("IMPORTANTISSIMO: ",supportFragmentManager.backStackEntryCount.toString())

    }

    private fun replaceFragmentProfile(fragment: Fragment,userId: String?,emailOff:String?){
        val idUtente=userId
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        intent.putExtra("idUtente",idUtente)
        intent.putExtra("email",emailOff)
        fragmentTransaction.replace(R.id.frameHomeLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameHomeLayout)
        val isOpen = intent.getStringExtra("isHomeOpen")
        Log.e("ho letto", isOpen.toString())
        Log.e("BACK:", currentFragment.toString())
        if (currentFragment is HomeFragmentNew && isOpen == "true") {
            super.onBackPressed()
            finishAffinity()
        } else {
            Log.e("SONO ELSE","ON BACK PRESSED")
            val manager = supportFragmentManager
            val idUtente = intent.getStringExtra("frame").toString()
            val address = resources.getIdentifier(idUtente, "id", packageName)
            manager.beginTransaction()
                .replace(address, HomeFragmentNew())
                .addToBackStack(null)
                .commit()
    }




}
}