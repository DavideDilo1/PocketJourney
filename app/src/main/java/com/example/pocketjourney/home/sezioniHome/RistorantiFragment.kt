package com.example.pocketjourney.home.sezioniHome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.home.AnteprimaPostoFragment
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.HomeAdapter
//import com.example.pocketjourney.adapter.MainRecyclerAdapter
import com.example.pocketjourney.adapter.HorizontalItemAdapter
import com.example.pocketjourney.databinding.FragmentRistorantiBinding
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem

class RistorantiFragment : Fragment() {

    private lateinit var binding: FragmentRistorantiBinding

    private lateinit var topImage:ImageView

    private lateinit var recyclerOrizzontale: RecyclerView
    private lateinit var recyclerVerticale: RecyclerView

    private lateinit var titoloFinestra: TextView
    private lateinit var testoTutti: TextView
    private lateinit var testoCategoria: TextView
    private lateinit var searchView: SearchView
    private lateinit var back_arrowR: ImageView

    private lateinit var anim_from_bottom: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRistorantiBinding.inflate(inflater)

        binding.RecyclerViewOrizzontaleR.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)



        //prima categoria
        val restaurantItemList1 = ArrayList<HorizontalRecyclerItem>()
        restaurantItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_one, "Bar di economia", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_one, "Bar di Ing", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_one, "Bar di Architettura", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_one, "Bar di Grande", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_one, "Bar di Pippo", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))


        val ristorantiOrizzAdapter1 = HorizontalItemAdapter(restaurantItemList1)
        binding.RecyclerViewOrizzontaleR.adapter = ristorantiOrizzAdapter1


        //seconda categoria
        val restaurantItemList2 = ArrayList<HorizontalRecyclerItem>()
        restaurantItemList2.add(HorizontalRecyclerItem(2,
            R.drawable.background, "Bar di Calogero", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(HorizontalRecyclerItem(2,
            R.drawable.background, "Bar di Lillo", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(HorizontalRecyclerItem(2,
            R.drawable.background, "Bar di Davide", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(HorizontalRecyclerItem(2,
            R.drawable.background, "Bar di Suor Carmela", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        restaurantItemList2.add(HorizontalRecyclerItem(2,
            R.drawable.background, "Bar di Cetto", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))

/*
        val allRestaurant: MutableList<AllRestaurant> = ArrayList()
        allRestaurant.add(AllRestaurant("I migliori ristoranti di sushi", restaurantItemList1))
        allRestaurant.add(AllRestaurant("Le migliori pizzerie", restaurantItemList2))
*/
      //  setHorizontalRecycler(allRestaurant)




        //  val ristorantiOrizzAdapter2 = RestaurantItemAdapter(requireContext(), restaurantItemList2)

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
/*
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

        }*/

        //QUESTA è LA RECYCLER VERTICALE CON TUTTI I RISTORANTI:

        val allTypeRestaurant = ArrayList<HomeItemModel>()
        //un ITEM VIEW MODEL é FATTO: val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float


        binding.RecyclerViewVerticaleR.layoutManager = LinearLayoutManager(requireContext())

        val ristorantiAdapter = HomeAdapter(allTypeRestaurant)
        binding.RecyclerViewVerticaleR.adapter = ristorantiAdapter






        back_arrowR = binding.backArrowR

        back_arrowR.setOnClickListener(){
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameRistoranti, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        topImage = binding.imageBackground
        recyclerOrizzontale = binding.RecyclerViewOrizzontaleR

        recyclerVerticale = binding.RecyclerViewVerticaleR

        titoloFinestra= binding.testoRistoranti

        testoTutti= binding.testoTuttiR
        testoCategoria= binding.testoCategoriaR
        searchView= binding.searchView


        anim_from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        anim_from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)


        topImage.animation = anim_from_top
        recyclerOrizzontale.animation =  anim_from_left
        recyclerVerticale.animation = anim_from_bottom
        testoTutti.animation = anim_from_left
        testoCategoria.animation = anim_from_left
        titoloFinestra.animation = anim_from_top
        searchView.animation = anim_from_right

        return binding.root
    }


}