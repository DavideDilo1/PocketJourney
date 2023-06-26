package com.example.pocketjourney.home.sezioniHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentPacchettiBinding

class PacchettiFragment : Fragment() {

    private lateinit var binding: FragmentPacchettiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPacchettiBinding.inflate(inflater)


        return binding.root
    }

}