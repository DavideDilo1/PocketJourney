package com.example.pocketjourney.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.RestaurantRowItemBinding
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.RestaurantItem

class RestaurantItemAdapter( private val restaurantItem: List<RestaurantItem> ): RecyclerView.Adapter<RestaurantItemAdapter.RestaurantItemViewHolder>(){

    var onItemClick : ((RestaurantItem) -> Unit)? = null

    class RestaurantItemViewHolder(binding: RestaurantRowItemBinding): RecyclerView.ViewHolder(binding.root){
        val imageView = binding.itemRestaurantImage
        val nomeRistorante = binding.titoloRistorante
        val numRecensioni = binding.numRecensioni
        val valutazione = binding.valutazioneRist
        val ratingBarRist = binding.ratingBarRestaurant
        val testoVario = binding.testoRistorante

    /*
        init {
            itemImage = itemView.findViewById(R.id.item_restaurant_Image)
        }*/
    }



    override fun onBindViewHolder( holder: RestaurantItemViewHolder, position: Int) {

        val RestaurantItemModel = restaurantItem[position]

        holder.imageView.setImageResource(RestaurantItemModel.imageUrl)

        holder.nomeRistorante.text = RestaurantItemModel.nomeRistorante

        holder.numRecensioni.text = RestaurantItemModel.numRec

        holder.valutazione.text = RestaurantItemModel.valutazione

        holder.ratingBarRist.rating = RestaurantItemModel.stelle

        holder.testoVario.text = RestaurantItemModel.testoVario

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(RestaurantItemModel)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantItemViewHolder {

        val view = RestaurantRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RestaurantItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return restaurantItem.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: RestaurantItem)
    }

}