package com.example.pocketjourney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.databinding.PrenotazioniCardViewBinding
import com.example.pocketjourney.model.PrenotazioniModel


class PrenotazioniAdapter(private val mList: List<PrenotazioniModel>) : RecyclerView.Adapter<PrenotazioniAdapter.prenotazioniHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): prenotazioniHolder {
        val view = PrenotazioniCardViewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return prenotazioniHolder(view)
    }
    //, mListener
    override fun onBindViewHolder(holder: prenotazioniHolder, position: Int) {
        val prenotazioniModel = mList[position]


        holder.nomePostoPrenotato.text = prenotazioniModel.nomePosto

        holder.dataPrenotazione.text = prenotazioniModel.data

        holder.numeroPersone.text = prenotazioniModel.numeroPersone

        holder.oraPrenotazione.text = prenotazioniModel.ora


    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class prenotazioniHolder(binding: PrenotazioniCardViewBinding) : RecyclerView.ViewHolder(binding.root) {

        val nomePostoPrenotato = binding.nomePostoPrenotato
        val dataPrenotazione = binding.dataPrenotazione
        val numeroPersone = binding.numeroPersonePrenotate
        val oraPrenotazione = binding.oraPrenotazione

    }

}
