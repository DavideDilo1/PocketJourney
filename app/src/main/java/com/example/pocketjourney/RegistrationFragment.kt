package com.example.pocketjourney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.pocketjourney.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private var dbManager: DBManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentRegistrationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegistrati.setOnClickListener {
            //Azione del pulsante di registrazione
            //prendo i valori degli edit text e li gestisco nella registrazione

            val Nome=binding.etNome.text.toString()
            val Cognome=binding.etCognome.text.toString()
            val email=binding.etEmailRegistrazione.text.toString()
            val password=binding.etPasswordRegistrazione.text.toString()
            val confermaPassword=binding.etConfermaPasswordRegistrazione.text.toString()
            val cellulare=binding.etNumeroCellulare.text.toString()

            //chiamo la funzione VerificaDatiRegistrazione
            if(VerificaDatiRegistrazione(Nome,Cognome,email,password,confermaPassword,cellulare)) {
                //inserisco l'utente
                dbManager = context?.let { it1 -> DBManager(it1) }
                if(dbManager!=null) {
                    dbManager?.open()
                    dbManager?.insert_utente(
                        Nome,
                        Cognome,
                        email,
                        password,
                        cellulare,
                        "prova",
                        "prova",
                        "prova"
                    )
                    requireActivity().supportFragmentManager.popBackStack()
                    Toast.makeText(requireContext(), "Registrazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Errore nell'ottenimento del contesto.", Toast.LENGTH_SHORT).show()
                }
            }

            }

        binding.btnTornaLogin.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            Toast.makeText(requireContext(), "Sei tornato a login", Toast.LENGTH_SHORT).show()
        }

        }

    override fun onDestroyView() {
        super.onDestroyView()

        val btnLogin = requireActivity().findViewById<Button>(R.id.btnLogin)
        val btnCreaAccount = requireActivity().findViewById<Button>(R.id.btnCreaAccount)
        btnLogin.visibility = View.VISIBLE
        btnCreaAccount.visibility = View.VISIBLE
    }

    fun verificaNome(nome: String): Boolean {
        val nomeRegex = Regex("^[A-Z][a-zA-Z]*$")
        val isNomeValido = nome.matches(nomeRegex)
        if (!isNomeValido) {
            Toast.makeText(context, "Inserisci un nome che inizi per lettera maiuscola e non contegna numeri.", Toast.LENGTH_SHORT).show()
        }
        return isNomeValido
    }

    fun verificaCognome(cognome: String): Boolean {
        val cognomeRegex = Regex("^[A-Z][a-zA-Z ]*$")
        val isCognomeValido = cognome.matches(cognomeRegex)
        if (!isCognomeValido) {
            Toast.makeText(context, "Inserisci un cognome valido che inizi per lettera maiuscola e non contegna numeri.", Toast.LENGTH_SHORT).show()
        }
        return isCognomeValido
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
        val emailRegex = Regex("^[A-Za-z]+\\.[A-Za-z]+@[A-Za-z]+\\.[A-Za-z]+$")
        val isEmailValida = email.matches(emailRegex)
        if (!isEmailValida) {
            Toast.makeText(context, "Inserisci un'email valida nel formato nome.cognome@provider.dominio", Toast.LENGTH_SHORT).show()
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

    fun VerificaDatiRegistrazione(
        nome: String,
        cognome: String,
        email: String,
        password: String,
        confermaPassword: String,
        numeroTelefono: String,
    ): Boolean {
        return (verificaNome(nome)
                && verificaCognome(cognome)
                && verificaPassword(password)
                && passwordUguali(password,confermaPassword)
                && verificaNumeroTelefono(numeroTelefono)
                && verificaEmail(email))
    }




}




