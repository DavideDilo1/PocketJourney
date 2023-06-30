package com.example.pocketjourney

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pocketjourney.accesso.MainActivity
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DbHelper
import com.example.pocketjourney.databinding.FragmentProfileBinding
import com.example.pocketjourney.profilo.CreditCardFragment
import com.example.pocketjourney.profilo.LeMieRecensioniFragment
import com.example.pocketjourney.profilo.ListaPrenotazioniFragment
import com.example.pocketjourney.profilo.ModificaDatiFragment
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var databaseHelper: DbHelper
    private var userId:Int=1 //variabile utile per l'interrogazione del database locale

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)

        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val emailUtenteOnline=requireActivity().intent.getStringExtra("emailOnline")
        val emailUtenteOffline = requireActivity().intent.getStringExtra("emailOff")

        if (idUtente != null ){
            //sono connesso a internet avendo ricevuto userId ed interrogo il database remoto
            Log.e("Ciao","sei al profile fragment e sei connesso")

            val userAPI= ClientNetwork.retrofit
            val queryNomeCognome = "SELECT Nome, Cognome, email, fotoProfilo FROM Utente WHERE idUtente = '$idUtente'"
            val call = userAPI.cerca(queryNomeCognome)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        //posso effettuare il login online
                        Log.e("Ciao","posso cambiare i dati con dbonline")
                        val jsonObject = response.body() // Ottieni il JSON come JsonObject

                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {
                            Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset" )
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                val primoUtente=querySetArray[0].asJsonObject //prendo la prima corrispondenza
                                Log.d("JSON", primoUtente.toString())

                                //verifico che non sia null e che contenga i campi corretti

                                if (primoUtente!=null && primoUtente.has("Nome") && primoUtente.has("Cognome") && primoUtente.has("email") && primoUtente.has("fotoProfilo")){
                                    //prelevo i campi e li setto nel fragment
                                    val nome=primoUtente.get("Nome").asString
                                    val cognome=primoUtente.get("Cognome").asString
                                    val email=primoUtente.get("email").asString
                                    val foto=primoUtente.get("fotoProfilo").asString

                                    binding.tvEmail.text = "${email}"
                                    binding.tvNomeCognomeProfilo.text = "${nome} ${cognome}"
                                    Log.e("Ciao", "HO CAMBIATO I DATI" )

                                    //setto l'immagine del profilo
                                    val downloadFotoProfilo=userAPI.getAvatar(foto)
                                    downloadFotoProfilo.enqueue(object :Callback<ResponseBody> {
                                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                            Log.e("Ciao", "sono dentro il blocco della foto" )
                                            Log.d("RESPONSE",response.isSuccessful.toString())
                                            if (response.isSuccessful){
                                                Log.e("Ciao", "sono dentro il blocco della foto DENTRO IS SUCCESSFULL" )
                                                val responseBody=response.body()
                                                if(responseBody!=null){
                                                    val inputStream=responseBody.byteStream()
                                                    val bitmap=BitmapFactory.decodeStream(inputStream)
                                                    //utilizza il Bitmap come immagine di profilo
                                                    binding.imgImmagineProfilo.setImageBitmap(bitmap)

                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                            Toast.makeText(requireContext(),"L'immagine non è stata trovaa correttamente",Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    //login in locale
                    Log.e("Ciao","posso cambiare i dati usando ONFAILURE")
                }
            })

        } else {
            //emailUtenteOnline è null quindi opero col db in locale sfruttando emailUtenteOffline
            Log.e("Ciao","CAMBIO I DATI CON L'ELSE ")
            databaseHelper= DbHelper(requireContext())

            //query al database locale per cercae l'utente
            userId= getUserIdByEmail(emailUtenteOffline)

            if(userId!=-1){
                val user=getUserById(userId)
                if(user!=null){
                    binding.tvEmail.text = emailUtenteOffline
                    binding.tvNomeCognomeProfilo.text = "${user?.nome} ${user?.cognome}"
                } else{
                    Toast.makeText(requireContext(), "L'utente non risulta nel database locale", Toast.LENGTH_SHORT).show()
                }
            }
        }

       //torna alla pagina di login resettando i campi inseriti precedentemente
       binding.btnLogout.setOnClickListener {
           requireActivity().supportFragmentManager.popBackStack()
           val intent = Intent(this.context, MainActivity::class.java)
           startActivity(intent)
        }

        //per accedere al fragment di modifica dei dati
        binding.btnModificaDati.setOnClickListener(View.OnClickListener { view ->
            val childFragment = ModificaDatiFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            requireActivity().intent.putExtra("emailUtenteOnline",emailUtenteOnline)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FragmentProfilo, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        })

        binding.prenotazioniButton.setOnClickListener{
            val childFragment = ListaPrenotazioniFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            requireActivity().intent.putExtra("emailUtenteOffline",emailUtenteOffline)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FragmentProfilo, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.pagamentiButton.setOnClickListener{
            val childFragment = CreditCardFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FragmentProfilo, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.recensioniButton.setOnClickListener {
            val childFragment = LeMieRecensioniFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FragmentProfilo, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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