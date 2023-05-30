package com.example.pocketjourney

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pocketjourney.databinding.FragmentProfileBinding
import com.example.pocketjourney.databinding.FragmentRegistrationBinding

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentProfileBinding.inflate(layoutInflater,container,false)

    /*    binding.btnModifica.setOnClickListener{
            //mi porta a fragment modifica dati
        }*/

    /*    binding.btnLogout.setOnClickListener {
        }*/

        return binding.root
    }


}