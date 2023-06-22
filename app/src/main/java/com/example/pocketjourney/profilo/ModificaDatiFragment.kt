package com.example.pocketjourney.profilo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DBManager
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

        binding.btnModifica.setOnClickListener {
            val nuovaEmail=binding.etNuovaEmail.text.toString()
            val nuovaPassword=binding.etModificaPassword.text.toString()
            val confermaNuovaPassword=binding.etConfermaPasswordMod.text.toString()
            val nuovoCellulare=binding.etCellulare.text.toString()
            //efettuo la modifica dei dati con un thread
            val scope = CoroutineScope(Dispatchers.Default)
            scope.launch {
                //query nel thread
                if (nuovaEmail.isNotEmpty() || nuovaPassword.isNotEmpty() || confermaNuovaPassword.isNotEmpty() || nuovoCellulare.isNotEmpty()) {
                    //è stato inserito almeno un nuovo dato e procedo a effettuare i vari controlli singolarmente
                    //creo il serverApi e il dbMa
                    Log.e("Ciao", "HO VISTO CHE C'è UN ALMENO UN VALORE RIEMPITO")
                    Log.d("emial nuova:", nuovaEmail.toString())
                    val serverAPI = ClientNetwork.retrofit
                    dbManager = context?.let { it1 -> DBManager(it1) }
                    //controllo la email
                    if (nuovaEmail.isNotEmpty() && verificaEmail(nuovaEmail)) {
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
                    if ((nuovaPassword.isNotEmpty() || confermaNuovaPassword.isNotEmpty()) && verificaPassword(nuovaPassword) && passwordUguali(nuovaPassword,confermaNuovaPassword)) {
                        //query per modificare la email sul server
                        Log.e("Ciao", "MODIFICO LA PASSWORD ")
                        Log.d("HAI INSERITO : ",nuovaPassword.toString() + confermaNuovaPassword.toString())
                        val queryModificaPassword = "UPDATE Utente SET password='$nuovaPassword' WHERE idUtente='$idUtente'"
                        val call = serverAPI.modifica(queryModificaPassword)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    Log.e("Ciao", "RISPOSTA PASSWORD AVVENUTA CON SUCCESSO")
                                    //posso modificare la email sia in locale che nel db remoto
                                    if (dbManager!=null){
                                        dbManager?.open()
                                        dbManager?.updatePasswordUtente(
                                            nuovaPassword
                                        )
                                        Log.e("Ciao", "MODIFICATO PASSWORD ANCHE IN LOCALE")

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
                    if (nuovoCellulare.isNotEmpty() && verificaNumeroTelefono(nuovoCellulare)) {
                        //query per modificare la email sul server
                        Log.e("Ciao", "MODIFICO CELLULARE ")
                        val queryModificaCellulare = "UPDATE Utente SET cellulare='$nuovoCellulare' WHERE idUtente='$idUtente'"
                        val call = serverAPI.modifica(queryModificaCellulare)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    Log.e("Ciao", "RISPOSTA CELLULARE AVVENUTA CON SUCCESSO")
                                    //posso modificare la email sia in locale che nel db remoto
                                    if (dbManager!=null){
                                        dbManager?.open()
                                        dbManager?.updateTelefonoUtente(
                                            nuovoCellulare
                                        )
                                        Log.e("Ciao", "MODIFICATO CELLULARE ANCHE IN LOCALE")

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


                }else {
                    Log.e("ciao","nessun valore inseritottttt")
                }
            }
        }

        binding.btnTornaProfilo.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(), "Nessun dato modificato", Toast.LENGTH_SHORT).show()
        }


    }







    fun verificaPassword(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[!@#%^&*()])(.{8,})")
        val isPasswordValida = password.matches(passwordRegex)
        if (!isPasswordValida) {
            activity?.runOnUiThread {
                Toast.makeText(context, "La password deve essere lunga 8 caratteri,contenere almeno una lettera maiuscola e un carattere speciale(!@#).", Toast.LENGTH_SHORT).show()
            }

        }
        return isPasswordValida
    }

    fun verificaNumeroTelefono(numeroTelefono: String): Boolean {
        val telefonoRegex = Regex("^\\d{10}$")
        val isNumeroTelefonoValido = numeroTelefono.matches(telefonoRegex)
        if (!isNumeroTelefonoValido) {
            activity?.runOnUiThread {
                Toast.makeText(activity, " inserisci una CELLULARE valido", Toast.LENGTH_SHORT).show()
            }
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
            activity?.runOnUiThread {
                Toast.makeText(context, "Le due password non coincidono", Toast.LENGTH_SHORT).show()
            }

            return false
        } else {
            return true
        }
    }


}