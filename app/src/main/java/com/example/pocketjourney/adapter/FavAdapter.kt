package com.example.pocketjourney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.databinding.CardViewDesignBinding
import com.example.pocketjourney.databinding.FavouriteCardViewDesignBinding
import com.example.pocketjourney.model.HomeItemModel

class FavAdapter (private val mList: List<HomeItemModel>) : RecyclerView.Adapter<FavAdapter.HomeViewHolder>() {


    private var onItemClick: ((HomeItemModel) -> Unit)? = null
    private var onToggleClickListener: ((HomeItemModel, Boolean) -> Unit)? = null

    fun setOnItemClickListener(listener: (HomeItemModel) -> Unit) {
        onItemClick = listener
    }

    fun setOnToggleClickListener(listener: (HomeItemModel, Boolean) -> Unit) {
        onToggleClickListener = listener
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = FavouriteCardViewDesignBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return HomeViewHolder(view)
    }
    //, mListener
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val HomeItemModel = mList[position]


        // sets the image to the imageview from our itemHolder class
        holder.viewHome.setImageBitmap(HomeItemModel.image)

        // sets the text to the textview from our itemHolder class

        holder.id.text = HomeItemModel.id.toString()

        holder.titleViewHome.text = HomeItemModel.title

        holder.ratingBarHome.rating = HomeItemModel.valutazione.toFloat()

        holder.numRecensioni.text = HomeItemModel.numRec

        holder.valutazione.text = HomeItemModel.valutazione

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(HomeItemModel)
        }

        holder.favButton.setOnCheckedChangeListener { buttonView, isChecked ->
            onToggleClickListener?.invoke(HomeItemModel, isChecked)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class HomeViewHolder(binding: FavouriteCardViewDesignBinding) : RecyclerView.ViewHolder(binding.root) {
        // , listener: onItemClickListener
        val id = binding.tvID
        val viewHome = binding.viewHome
        val titleViewHome = binding.titleViewHome
        val ratingBarHome = binding.ratingBar
        val numRecensioni = binding.numRecensioniHome
        val valutazione = binding.valutazioneHome
        val favButton = binding.FavoriteButton2

    }

}




