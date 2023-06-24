package com.example.pocketjourney.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentBookingBinding

class BookingFragment : Fragment() {

    private lateinit var binding: FragmentBookingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBookingBinding.inflate(inflater)

        binding.buttonPrenota.setOnClickListener{

            Toast.makeText(requireContext(), "Prenotazione avvenuta con successo", Toast.LENGTH_SHORT ).show()
        }





        return binding.root
    }

}