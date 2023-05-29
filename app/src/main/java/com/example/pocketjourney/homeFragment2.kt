package com.example.pocketjourney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.databinding.FragmentHome2Binding

class homeFragment2 : Fragment() {

    private lateinit var binding: FragmentHome2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHome2Binding.inflate(inflater)

        //I bottoni serviranno da filtro o da altro?

        binding.ristorantiButton.setOnClickListener {
          // TODO: Implementare comportamento
        }
        binding.attrazioniButton.setOnClickListener {
            // TODO: Implementare comportamento

        }
        binding.hotelsButton.setOnClickListener {
            // TODO: Implementare comportamento

        }

        return binding.root
    }



}

