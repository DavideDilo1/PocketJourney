package com.example.pocketjourney.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.R
import com.example.pocketjourney.model.RestaurantItem

class RestaurantItemAdapter(private val context: Context, private val restaurantItem: List<RestaurantItem> ): RecyclerView.Adapter<RestaurantItemAdapter.RestaurantItemViewHolder>(){

    class RestaurantItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemImage: ImageView
        init {
            itemImage = itemView.findViewById(R.id.item_restaurant_Image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantItemAdapter.RestaurantItemViewHolder {
        return RestaurantItemViewHolder(LayoutInflater.from(context).inflate(R.layout.restaurant_row_item, parent, false))
    }

    override fun onBindViewHolder(
        holder: RestaurantItemAdapter.RestaurantItemViewHolder,
        position: Int
    ) {
        holder.itemImage.setImageResource(restaurantItem[position].imageUrl)

        val item = restaurantItem[position]

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }

    }

    override fun getItemCount(): Int {
        return restaurantItem.size
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: RestaurantItem)
    }

}