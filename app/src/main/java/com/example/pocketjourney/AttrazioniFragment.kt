package com.example.pocketjourney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.databinding.FragmentAttrazioniBinding


class AttrazioniFragment : Fragment() {

private lateinit var binding: FragmentAttrazioniBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttrazioniBinding.inflate(inflater)

        return binding.root
    }

}