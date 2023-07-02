package com.example.pocketjourney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.databinding.HorizontalViewDesignBinding
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem

class HorizontalItemAdapter(private val horizontalRecyclerItem: List<HorizontalRecyclerItem> ): RecyclerView.Adapter<HorizontalItemAdapter.RestaurantItemViewHolder>(){

    var onItemClick : ((HorizontalRecyclerItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (HorizontalRecyclerItem) -> Unit) {
        onItemClick = listener
    }

    class RestaurantItemViewHolder(binding: HorizontalViewDesignBinding): RecyclerView.ViewHolder(binding.root){
        val id=binding.tvID
        val imageView = binding.itemImage
        val nomeRistorante = binding.titolo
        val numRecensioni = binding.numRecensioni
        val valutazione = binding.valutazioneItem
        val ratingBarRist = binding.ratingBarItem
        val testoVario = binding.testo


    }



    override fun onBindViewHolder( holder: RestaurantItemViewHolder, position: Int) {

        val RestaurantItemModel = horizontalRecyclerItem[position]

        holder.id.text=RestaurantItemModel.id.toString()

        holder.imageView.setImageBitmap(RestaurantItemModel.imageUrl)

        holder.nomeRistorante.text = RestaurantItemModel.nomeRistorante

        holder.numRecensioni.text = RestaurantItemModel.numRec

        holder.valutazione.text = RestaurantItemModel.valutazione

        holder.ratingBarRist.rating=RestaurantItemModel.valutazione.toFloat()

        holder.testoVario.text = RestaurantItemModel.testoVario

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(RestaurantItemModel)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemViewHolder {

        val view = HorizontalViewDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RestaurantItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return horizontalRecyclerItem.size
    }

}