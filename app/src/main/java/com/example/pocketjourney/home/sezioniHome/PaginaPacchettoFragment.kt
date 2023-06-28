package com.example.pocketjourney.home.sezioniHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentPaginaPacchettoBinding


class PaginaPacchettoFragment : Fragment() {

    private lateinit var binding: FragmentPaginaPacchettoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentPaginaPacchettoBinding.inflate(inflater)

        //TODO: AGGIUNGERE IL CORRETTO CAMBIO DI FRAGMENT
        binding.scopriTutteLeRec2.setOnClickListener {
            //fai comparire fragment recensioni

        }
        //TODO: INSERIRE IN DATALIST LE RECENSIONI COSI DA LIMITARE IL NUMERO DI REC VISIBILI DALLA SCHERMATA POSTO

        // val datalist = //lista delle recensioni

        val maxItemsToShow = 3

        //val adapter = RecensioniAdapter(dataList, maxItemsToShow)

        //   binding.recyclerPaginaPostoRecensioni.adapter = adapter





        return binding.root
    }

}