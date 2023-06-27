package com.example.pocketjourney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.databinding.RecensioniCardViewDesignBinding
import com.example.pocketjourney.model.RecensioniModel

class RecensioniAdapter(private val mList: List<RecensioniModel>) : RecyclerView.Adapter<RecensioniAdapter.recensioniHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): recensioniHolder {
        val view = RecensioniCardViewDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return recensioniHolder(view)
    }

    override fun onBindViewHolder(holder: recensioniHolder, position: Int) {
        val recensioniModel = mList[position]


        holder.nomeRecensore.text = recensioniModel.nomeRecensore

        holder.titoloRecensione.text = recensioniModel.titoloRecensione

        holder.testoRecensione.text = recensioniModel.testoRecensione

        holder.ratingBarRec.rating = recensioniModel.numStelle
    }


    override fun getItemCount(): Int {
        return mList.size
        
    }

    class recensioniHolder(binding: RecensioniCardViewDesignBinding) : RecyclerView.ViewHolder(binding.root){

        val nomeRecensore = binding.nomeRecensore
        val titoloRecensione = binding.titoloRecensione
        val testoRecensione = binding.testoRecensione
        val ratingBarRec = binding.ratingBarRecensioneLay
    }

}