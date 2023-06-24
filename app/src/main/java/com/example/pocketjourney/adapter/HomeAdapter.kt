package com.example.pocketjourney.adapter

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.databinding.CardViewDesignBinding
import com.example.pocketjourney.model.HomeItemModel

class HomeAdapter(private val mList: List<HomeItemModel>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {


    private var onItemClick: ((HomeItemModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (HomeItemModel) -> Unit) {
        onItemClick = listener
    }

   // private lateinit var mListener: onItemClickListener
    /*
    interface onItemClickListener{
        fun onItemClick(position: Int){

        }
    }

    fun setOnItemClickListener(listener: onItemClickListener){

        mListener = listener

    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = CardViewDesignBinding.inflate(
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

        /* Finally add an onclickListener to the item.
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, HomeItemModel)
        }
*/
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(HomeItemModel)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }


    class HomeViewHolder(binding: CardViewDesignBinding) : RecyclerView.ViewHolder(binding.root) {
       // , listener: onItemClickListener
        val id = binding.tvID
        val viewHome = binding.viewHome
        val titleViewHome = binding.titleViewHome
        val ratingBarHome = binding.ratingBar
        val numRecensioni = binding.numRecensioniHome
        val valutazione = binding.valutazioneHome

        }

}








