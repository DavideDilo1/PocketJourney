package com.example.pocketjourney.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentRecensioniBinding

class RecensioniFragment : Fragment() {

    private lateinit var binding: FragmentRecensioniBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecensioniBinding.inflate(inflater)


        return binding.root
    }

}