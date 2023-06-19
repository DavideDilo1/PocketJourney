package com.example.pocketjourney

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.adapter.HomeAdapter
import com.example.pocketjourney.adapter.MainRecyclerAdapter
import com.example.pocketjourney.adapter.RestaurantItemAdapter
import com.example.pocketjourney.databinding.FragmentRistorantiBinding
import com.example.pocketjourney.model.AllRestaurant
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.RestaurantItem

class RistorantiFragment : Fragment() {

    private lateinit var binding: FragmentRistorantiBinding

    private var mainCategoryRecycler: RecyclerView? = null
    private var mainRecyclerAdapter:MainRecyclerAdapter? = null

    private var recyclerViewOrizzontale: RecyclerView? = null
    private var recyclerViewVerticale: RecyclerView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRistorantiBinding.inflate(inflater)

        //qui si aggiungono i dati al modello della classe

        //prima categoria
        val restaurantItemList1: MutableList<RestaurantItem> = ArrayList()
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background, "Bar di economia", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background, "Bar di Ing", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background, "Bar di Architettura", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background, "Bar di Grande", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(RestaurantItem(1,R.drawable.background, "Bar di Pippo", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))

        //seconda categoria
        val restaurantItemList2: MutableList<RestaurantItem> = ArrayList()
        restaurantItemList2.add(RestaurantItem(2,R.drawable.background, "Bar di Calogero", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(RestaurantItem(2,R.drawable.background, "Bar di Lillo", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(RestaurantItem(2,R.drawable.background, "Bar di Davide", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(RestaurantItem(2,R.drawable.background, "Bar di Suor Carmela", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(RestaurantItem(2,R.drawable.background, "Bar di Cetto", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))


        val allRestaurant: MutableList<AllRestaurant> = ArrayList()
        allRestaurant.add(AllRestaurant("I migliori ristoranti di sushi", restaurantItemList1))
        allRestaurant.add(AllRestaurant("Le migliori pizzerie", restaurantItemList2))

        setHorizontalRecycler(allRestaurant)

        val ristorantiOrizzAdapter1 = RestaurantItemAdapter(requireContext(), restaurantItemList1)
        val ristorantiOrizzAdapter2 = RestaurantItemAdapter(requireContext(), restaurantItemList2)

//TODO: QUESTO NON FUNZIONA!!! AGGIUSTARLO
        ristorantiOrizzAdapter1.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("ristoranti", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle
            Log.i("Ristoranti", "ALmeno qua ci entro e creo il fragment?")

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameRistoranti, childFragment)
                .addToBackStack(null)
                .commit()

        }

        ristorantiOrizzAdapter2.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("ristoranti", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameRistoranti, childFragment)
                .addToBackStack(null)
                .commit()

        }


//TODO: Aggiungere i click listener per gli item della nested

        //QUESTA è LA RECYCLER VERTICALE CON TUTTI I RISTORANTI:

        val allTypeRestaurant = ArrayList<HomeItemModel>()
        //un ITEM VIEW MODEL é FATTO: val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float

        allTypeRestaurant.add(HomeItemModel(R.drawable.image_one, "The START Hotel, Casino &amp; SkyPod", "(510)", "4.91", 4.5F ))
        allTypeRestaurant.add(HomeItemModel(R.drawable.image_two, "Bar di economia", "(510)", "4.91", 4.5F ))
        allTypeRestaurant.add(HomeItemModel(R.drawable.image_three, "Bar di architettura", "(510)", "4.91", 4.5F ))

        binding.RecyclerViewVerticale.layoutManager = LinearLayoutManager(requireContext())

        val ristorantiAdapter = HomeAdapter(allTypeRestaurant)
        binding.RecyclerViewVerticale.adapter = ristorantiAdapter



        ristorantiAdapter.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("ristoranti", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameRistoranti, childFragment)
                .addToBackStack(null)
                .commit()

        }





        return binding.root
    }

    private fun setHorizontalRecycler(allRestaurant: List<AllRestaurant>){
        mainCategoryRecycler = binding.RecyclerViewOrizzontale
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        mainCategoryRecycler!!.layoutManager = layoutManager
        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), allRestaurant)
        mainCategoryRecycler!!.adapter = mainRecyclerAdapter

    }

}