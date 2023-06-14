package com.example.pocketjourney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.databinding.FragmentHotelBinding
import com.example.pocketjourney.databinding.FragmentRistorantiBinding

class RistorantiFragment : Fragment() {

    private lateinit var binding: FragmentRistorantiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRistorantiBinding.inflate(inflater)
        return binding.root    }

}