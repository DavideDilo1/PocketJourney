package com.example.pocketjourney

import android.os.Binder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.pocketjourney.databinding.FragmentModificaDatiBinding
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ModificaDatiFragment : Fragment() {
    private lateinit var binding: FragmentModificaDatiBinding
    private var dbManager: DBManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentModificaDatiBinding.inflate(layoutInflater,container,false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ottengo l'id dell'utente col quale interrogherò il db e i dati che mi servono
        val idUtente = arguments?.getString("idUtente")
        val emailOnline=arguments?.getString("emailOnline")
        Log.d("Ciao",idUtente.toString())
        Log.d("Ciao",emailOnline.toString())
        Log.e("Ciao", "SEI NEL FRAGMENT MODIFICA")
        val nuovaPassword=binding.etPasswordRegistrazione.text.toString()
        val confermaNuovaPassword=binding.etConfermaPasswordRegistrazione.text.toString()
        val nuovoCellulare=binding.etNumeroCellulare.text.toString()
        binding.btnModificaDati.setOnClickListener {
            val nuovaEmail=binding.etEmailRegistrazione.text.toString()
            //efettuo la modifica dei dati con un thread
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                //query nel thread
                if (nuovaEmail != null || nuovaPassword != null || confermaNuovaPassword != null || nuovoCellulare != null) {
                    //è stato inserito almeno un nuovo dato e procedo a effettuare i vari controlli singolarmente
                    //creo il serverApi e il dbMa
                    Log.e("Ciao", "HO VISTO CHE C'è UN ALMENO UN VALORE RIEMPITO")
                    Log.d("emial nuova:", nuovaEmail.toString())
                    val serverAPI = ClientNetwork.retrofit
                    dbManager = context?.let { it1 -> DBManager(it1) }
                    //controllo la email
                    if (nuovaEmail != null && verificaEmail(nuovaEmail)) {
                        //query per modificare la email sul server
                        Log.e("Ciao", "MODIFICO EMAIL ")
                        val queryModificaEmail = "UPDATE Utente SET email='$nuovaEmail' WHERE idUtente='$idUtente'"
                        val call = serverAPI.modifica(queryModificaEmail)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    Log.e("Ciao", "RISPOSTA AVVENUTA CON SUCCESSO")
                                        //posso modificare la email sia in locale che nel db remoto
                                        if (dbManager!=null){
                                            dbManager?.open()
                                            dbManager?.updateEmailUtente(
                                                emailOnline!!
                                            )
                                            Log.e("Ciao", "MODIFICATO ANCHE IN LOCALE")

                                        }

                                } else {
                                    // Si è verificato un errore nella richiesta online
                                    Log.e("Ciao", "ERRORE ")

                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                // Si è verificato un errore durante la chiamata di rete online
                                Log.e("Ciao", "ERRORE DI CONNESSIONE")
                            }
                        })
                    }


                }
            }
        }


    }







    fun verificaPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!@#%^&*()])(.{8,})")
        val isPasswordValida = password.matches(passwordRegex)
        if (!isPasswordValida) {
            Toast.makeText(context, "La password deve essere lunga 8 caratteri,contenere almeno una lettera maiuscola e un carattere speciale(!@#).", Toast.LENGTH_SHORT).show()
        }
        return isPasswordValida
    }

    fun verificaNumeroTelefono(numeroTelefono: String): Boolean {
        val telefonoRegex = Regex("^\\d{10}$")
        val isNumeroTelefonoValido = numeroTelefono.matches(telefonoRegex)
        if (!isNumeroTelefonoValido) {
            Toast.makeText(context, "Inserisci un numero di telefono valido che abbia esattamente 10 numeri e non contenga spazi.", Toast.LENGTH_SHORT).show()
        }
        return isNumeroTelefonoValido
    }

    fun verificaEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9]+\\.[A-Za-z0-9]+@[A-Za-z]+\\.[A-Za-z]+$")
        val emailRegex2 = Regex("^[A-Za-z0-9]+@[A-Za-z]+\\.[A-Za-z]+$")
        val isEmailValida = (email.matches(emailRegex) || email.matches(emailRegex2))
        if (!isEmailValida) {
            activity?.runOnUiThread {
                Toast.makeText(activity, "inserisci una mail valida", Toast.LENGTH_SHORT).show()
            }

        }
        return isEmailValida
    }

    fun passwordUguali(password: String,confermaPassword: String): Boolean {
        if (password!=confermaPassword) {
            Toast.makeText(context, "Le due password non coincidono", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }


}