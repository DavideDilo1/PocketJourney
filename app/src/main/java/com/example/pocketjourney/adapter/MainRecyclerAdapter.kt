package com.example.pocketjourney.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.R
import com.example.pocketjourney.model.AllRestaurant
import com.example.pocketjourney.model.RestaurantItem

class MainRecyclerAdapter(private val context: Context, private val allRestaurant: List<AllRestaurant>) :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var restaurantTitle: TextView
        var itemRecycler: RecyclerView
        init{
            restaurantTitle = itemView.findViewById(R.id.cat_title)
            itemRecycler = itemView.findViewById(R.id.restaurant_item_recycler )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_restaurant_recycler_row_item, parent, false))
    }

    override fun getItemCount(): Int {
        return allRestaurant.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.restaurantTitle.text = allRestaurant[position].categoryTitle
        setResturantItemRecycler(holder.itemRecycler, allRestaurant[position].restaurantItem)
    }

    private fun setResturantItemRecycler(recyclerView: RecyclerView, restaurantItem: List<RestaurantItem>){
        val itemRecyclerAdapter = RestaurantItemAdapter(context, restaurantItem)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter
    }

}