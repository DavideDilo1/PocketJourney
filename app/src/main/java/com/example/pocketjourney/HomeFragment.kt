package com.example.pocketjourney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import com.example.pocketjourney.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        //ricerca filtrante:
/*
        val user = arrayOf("Ciao1 ", " Ciao2", " Ciao3", " Ciao4" )

        val userAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1,
            user
        )


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                //esegui la ricerca
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                //gestisci il cambio di testo durante la digitazione
                //aggiornare la lista dei risultati in base al nuovo testo
            }

        })



        */
        return binding.root
    }
}


