package com.example.pocketjourney.preferiti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.R

class ListaPreferitiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_preferiti, container, false)
    }





    fun aggiungiPreferitiClick(view: View) {
//TODO: implementare la vera aggiunta ai preferiti
        Toast.makeText(requireContext(), "AggiungiPreferiti", Toast.LENGTH_SHORT).show()

    }
}