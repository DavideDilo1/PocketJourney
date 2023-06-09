package com.example.pocketjourney.profilo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.ProfileFragment
import com.example.pocketjourney.R
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
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val emailUtenteOnline=requireActivity().intent.getStringExtra("email")
        requireActivity().intent.putExtra("frame","modificaDatiFragment")


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

                    val serverAPI = ClientNetwork.retrofit
                    dbManager = context?.let { it1 -> DBManager(it1) }
                    //controllo la email
                    if (nuovaEmail.isNotEmpty() && verificaEmail(nuovaEmail)) {
                        //query per modificare la email sul server
                        val queryModificaEmail = "UPDATE Utente SET email='$nuovaEmail' WHERE idUtente='$idUtente'"
                        val call = serverAPI.modifica(queryModificaEmail)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                        //posso modificare la email sia in locale che nel db remoto
                                        if (dbManager!=null){
                                            dbManager?.open()
                                            dbManager?.updateEmailUtente(
                                                nuovaEmail,
                                                emailUtenteOnline.toString()
                                            )
                                            dbManager?.close()

                                        }

                                } else {
                                    // Si è verificato un errore nella richiesta online
                                    Toast.makeText(context, "Si è verificato un errore di rete.", Toast.LENGTH_SHORT).show()

                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                // Si è verificato un errore durante la chiamata di rete online
                                Toast.makeText(context, "Si è verificato un errore di rete.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    if ((nuovaPassword.isNotEmpty() || confermaNuovaPassword.isNotEmpty()) && verificaPassword(nuovaPassword) && passwordUguali(nuovaPassword,confermaNuovaPassword)) {
                        //query per modificare la email sul server

                        val queryModificaPassword = "UPDATE Utente SET password='$nuovaPassword' WHERE idUtente='$idUtente'"
                        val call = serverAPI.modifica(queryModificaPassword)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    //posso modificare la email sia in locale che nel db remoto
                                    if (dbManager!=null){
                                        dbManager?.open()
                                        dbManager?.updatePasswordUtente(
                                            emailUtenteOnline.toString(),
                                            nuovaPassword
                                        )

                                    }

                                } else {
                                    // Si è verificato un errore nella richiesta online
                                    Toast.makeText(context, "Si è verificato un errore di rete.", Toast.LENGTH_SHORT).show()

                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                // Si è verificato un errore durante la chiamata di rete online
                                Toast.makeText(context, "Si è verificato un errore di rete.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    if (nuovoCellulare.isNotEmpty() && verificaNumeroTelefono(nuovoCellulare)) {
                        //query per modificare la email sul server
                        val queryModificaCellulare = "UPDATE Utente SET cellulare='$nuovoCellulare' WHERE idUtente='$idUtente'"
                        val call = serverAPI.modifica(queryModificaCellulare)
                        call.enqueue(object : Callback<JsonObject> {
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                if (response.isSuccessful) {
                                    //posso modificare la email sia in locale che nel db remoto
                                    if (dbManager!=null){
                                        dbManager?.open()
                                        dbManager?.updateTelefonoUtente(
                                            emailUtenteOnline.toString(),
                                            nuovoCellulare
                                        )

                                    }

                                } else {
                                    // Si è verificato un errore nella richiesta online
                                    Toast.makeText(context, "Si è verificato un errore di rete.", Toast.LENGTH_SHORT).show()

                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                // Si è verificato un errore durante la chiamata di rete online
                                Toast.makeText(context, "Si è verificato un errore di rete.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }


                }else {
                    Toast.makeText(context, "Nessun valore inserito.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnTornaProfilo.setOnClickListener{
            binding.btnModifica.visibility=View.GONE
            binding.btnTornaProfilo.visibility=View.GONE
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.modificaDatiFragment, ProfileFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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