package com.example.pocketjourney.profilo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentLeMieRecensioniBinding

class LeMieRecensioniFragment : Fragment() {
    private lateinit var binding: FragmentLeMieRecensioniBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLeMieRecensioniBinding.inflate(inflater)


        return binding.root
    }

}