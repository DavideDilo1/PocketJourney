package com.example.pocketjourney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.databinding.HorizontalViewDesignBinding
import com.example.pocketjourney.model.HorizontalRecyclerItem

class HorizontalItemAdapter(private val horizontalRecyclerItem: List<HorizontalRecyclerItem> ): RecyclerView.Adapter<HorizontalItemAdapter.RestaurantItemViewHolder>(){

    var onItemClick : ((HorizontalRecyclerItem) -> Unit)? = null

    class RestaurantItemViewHolder(binding: HorizontalViewDesignBinding): RecyclerView.ViewHolder(binding.root){
        val imageView = binding.itemImage
        val nomeRistorante = binding.titolo
        val numRecensioni = binding.numRecensioni
        val valutazione = binding.valutazioneItem
        val ratingBarRist = binding.ratingBarItem
        val testoVario = binding.testo

    /*
        init {
            itemImage = itemView.findViewById(R.id.item_restaurant_Image)
        }*/
    }



    override fun onBindViewHolder( holder: RestaurantItemViewHolder, position: Int) {

        val RestaurantItemModel = horizontalRecyclerItem[position]

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

        val view = HorizontalViewDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RestaurantItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return horizontalRecyclerItem.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: HorizontalRecyclerItem)
    }

}