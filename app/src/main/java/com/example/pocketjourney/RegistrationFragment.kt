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

            if(password==confermaPassword){
                requireActivity().supportFragmentManager.popBackStack()
                Toast.makeText(requireContext(), "Registrazione completata!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Le pw non coincidono", Toast.LENGTH_SHORT).show()
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
}




