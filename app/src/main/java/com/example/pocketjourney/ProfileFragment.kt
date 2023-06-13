package com.example.pocketjourney

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pocketjourney.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var databaseHelper: DbHelper
    private var userId:Int=1 //variabile utile per l'interrogazione del database locale

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)

        val emailUtenteOnline = arguments?.getString("emailutenteOnline")
        val emailUtenteOffline = arguments?.getString("emailutenteOffline")

        if (emailUtenteOnline != null){
            //sono connesso a internet avendo ricevuto idUtente
            Log.e("Ciao","sei al profile fragment e sei connesso")
        } else {
            Log.e("Ciao","sei al profile fragment non sei connesso")
            databaseHelper=DbHelper(requireContext())

            userId= getUserIdByEmail(emailUtenteOffline)

            if(userId!=-1){
                val user=getUserById(userId)
                if(user!=null){
                    binding.tvEmail.text = emailUtenteOffline
                    binding.tvNomeCognomeProfilo.text = "${user?.nome} ${user?.cognome}"
                } else{
                    Toast.makeText(requireContext(), "Utente non risulta nel database", Toast.LENGTH_SHORT).show()
                }
            }
        }

       binding.btnLogout.setOnClickListener {
           requireActivity().supportFragmentManager.popBackStack()
           val intent = Intent(this.context, MainActivity::class.java)
           startActivity(intent)
        }

        return binding.root
    }

    private fun getUserIdByEmail(email: String?): Int {
        val db = databaseHelper.readableDatabase
        var userId = -1

        val query = "SELECT id FROM Utenti WHERE email=?"
        if (email != null) {
            val cursor = db.rawQuery(query, arrayOf(email))
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            }
            cursor.close()
        }
        return userId
    }

    private fun getUserById(userId: Int): User? {
        val db = databaseHelper.readableDatabase
        val columns = arrayOf("Nome", "Cognome", "email")
        val selection = "id = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor = db.query("Utenti", columns, selection, selectionArgs, null, null, null)

        if (cursor.moveToFirst()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("Nome"))
            val cognome = cursor.getString(cursor.getColumnIndexOrThrow("Cognome"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            cursor.close()
            return User(nome, cognome, email)
        }
        cursor.close()
        return null
    }


}
data class User(val nome: String, val cognome: String, val email: String)