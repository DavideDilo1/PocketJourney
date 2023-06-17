package com.example.pocketjourney.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pocketjourney.databinding.CardViewDesignBinding
import com.example.pocketjourney.model.HomeItemModel

class HomeAdapter(private val mList: List<HomeItemModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewDesignBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(view)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val HomeItemModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.viewHome.setBackgroundResource(HomeItemModel.image)

        // sets the text to the textview from our itemHolder class
        holder.titleViewHome.setText(HomeItemModel.title)

        holder.ratingBarHome.rating = HomeItemModel.stelle

        holder.numRecensioni.setText(HomeItemModel.numRec)

        holder.valutazione.setText(HomeItemModel.valutazione)

        // Finally add an onclickListener to the item.
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, HomeItemModel)
        }    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(binding: CardViewDesignBinding) : RecyclerView.ViewHolder(binding.root) {
        val viewHome = binding.viewHome
        val titleViewHome = binding.titleViewHome
        val ratingBarHome = binding.ratingBar
        val numRecensioni = binding.numRecensioniHome
        val valutazione = binding.valutazioneHome

    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: HomeItemModel)
    }


}