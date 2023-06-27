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
// Calcola la lunghezza della ProgressBar per ciascuna categoria basata sul numero di recensioni
        val numRecensioniEccellenti = 100 // Numero di recensioni eccellenti
        val numRecensioniMoltoBuone = 75 // Numero di recensioni molto buone
        val numRecensioniNellaMedia = 50 // Numero di recensioni nella media
        val numRecensioniScarse = 20
        val numRecensioniTerribili =10


        val excellentProgressBar = binding.excellentProgressBar
        val goodProgressBar = binding.goodProgressBar
        val averageProgressBar = binding.averageProgressBar
        val poorProgressBar = binding.poorProgressBar
        val terribleProgressBar = binding.terribleProgressBar

        excellentProgressBar.progress = numRecensioniEccellenti
        goodProgressBar.progress = numRecensioniMoltoBuone
        averageProgressBar.progress = numRecensioniNellaMedia
        poorProgressBar.progress = numRecensioniScarse
        terribleProgressBar.progress = numRecensioniTerribili

        return binding.root
    }

}