package com.example.pocketjourney

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.pocketjourney.databinding.FragmentModificaDatiBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class ModificaDatiFragment : Fragment() {
    private lateinit var binding: FragmentModificaDatiBinding
    private lateinit var databaseHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentModificaDatiBinding.inflate(layoutInflater,container,false)
        //ottengo l'id dell'utente col quale interrogherò il db e i dati che mi servono
        val idUtente = arguments?.getString("idUtente")

        val nuovaEmail=binding.etEmailRegistrazione.text.toString()
        val nuovaPassword=binding.etPasswordRegistrazione.text.toString()
        val confermaNuovaPassword=binding.etConfermaPasswordRegistrazione.text.toString()
        val nuovoCellulare=binding.etNumeroCellulare.text.toString()
        val scope= CoroutineScope(Dispatchers.Default)
        scope.launch {
            //query nel thread
            if(nuovaEmail!=null || nuovaPassword!=null || confermaNuovaPassword!=null || nuovoCellulare!=null) {
                //è stato inserito almeno un nuovo dato e procedo a effettuare i vari controlli singolarmente

            }
        }


        return binding.root
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
            Toast.makeText(context, "Inserisci un'email valida", Toast.LENGTH_SHORT).show()
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