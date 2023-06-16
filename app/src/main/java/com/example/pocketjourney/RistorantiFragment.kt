package com.example.pocketjourney

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.adapter.MainRecyclerAdapter
import com.example.pocketjourney.databinding.FragmentRistorantiBinding
import com.example.pocketjourney.model.AllRestaurant
import com.example.pocketjourney.model.RestaurantItem

class RistorantiFragment : Fragment() {

    private lateinit var binding: FragmentRistorantiBinding

    private var mainCategoryRecycler: RecyclerView? = null
    private var mainRecyclerAdapter:MainRecyclerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRistorantiBinding.inflate(inflater)

        //qui si aggiungono i dati al modello della classe

        //prima categoria
        val restaurantItemList1: MutableList<RestaurantItem> = ArrayList()
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background))

        //seconda categoria
        val restaurantItemList2: MutableList<RestaurantItem> = ArrayList()
        restaurantItemList2.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList2.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList2.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList2.add(RestaurantItem(1,R.drawable.background))
        restaurantItemList2.add(RestaurantItem(1,R.drawable.background))


        val allRestaurant: MutableList<AllRestaurant> = ArrayList()
        allRestaurant.add(AllRestaurant("I migliori ristoranti di sushi", restaurantItemList1))
        allRestaurant.add(AllRestaurant("Le migliori pizzerie",restaurantItemList2))

        setMainCategoryRecycler(allRestaurant)


        return binding.root
    }

    private fun setMainCategoryRecycler(allRestaurant: List<AllRestaurant>){
        mainCategoryRecycler = binding.mainRecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        mainCategoryRecycler!!.layoutManager = layoutManager
        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), allRestaurant)
        mainCategoryRecycler!!.adapter = mainRecyclerAdapter

    }

}