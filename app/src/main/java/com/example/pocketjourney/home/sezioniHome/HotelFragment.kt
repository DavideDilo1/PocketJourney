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
import com.example.pocketjourney.adapter.HorizontalItemAdapter
import com.example.pocketjourney.databinding.FragmentHotelBinding
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem


class HotelFragment : Fragment() {

    private lateinit var binding: FragmentHotelBinding

    private lateinit var back_arrowH: ImageView

    private lateinit var topImage:ImageView

    private lateinit var recyclerOrizzontale: RecyclerView
    private lateinit var recyclerVerticale: RecyclerView

    private lateinit var titoloFinestra: TextView
    private lateinit var testoTutti: TextView
    private lateinit var testoCategoria: TextView
    private lateinit var searchView: SearchView


    private lateinit var anim_from_bottom: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHotelBinding.inflate(inflater)

        binding.RecyclerViewOrizzontaleH.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val hotelItemList1 = ArrayList<HorizontalRecyclerItem>()
        hotelItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_two, "Bar di economia", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        hotelItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_two, "Bar di Ing", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        hotelItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_two, "Bar di Architettura", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        hotelItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_two, "Bar di Grande", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))
        hotelItemList1.add(HorizontalRecyclerItem(1,
            R.drawable.image_two, "Bar di Pippo", "(510)", "4.91", 4.5F, "Questo ristorante è molto bello"))


        val hotelItemListAdapter1 = HorizontalItemAdapter(hotelItemList1)
        binding.RecyclerViewOrizzontaleH.adapter = hotelItemListAdapter1


        hotelItemListAdapter1.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("hotel", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle
            Log.i("Hotel", "ALmeno qua ci entro e creo il fragment?")

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameHotel, childFragment)
                .addToBackStack(null)
                .commit()

        }


        val allTypeHotel = ArrayList<HomeItemModel>()
        //un ITEM VIEW MODEL é FATTO: val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float


        binding.RecyclerViewVerticaleH.layoutManager = LinearLayoutManager(requireContext())

        val hotelAdapter = HomeAdapter(allTypeHotel)
        binding.RecyclerViewVerticaleH.adapter = hotelAdapter



        hotelAdapter.onItemClick = {


            val childFragment = AnteprimaPostoFragment()


            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameHotel, childFragment)
                .addToBackStack(null)
                .commit()

        }


        back_arrowH= binding.backArrowH

        back_arrowH.setOnClickListener(){
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameHotel, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        topImage = binding.imageBackground
        recyclerOrizzontale = binding.RecyclerViewOrizzontaleH

        recyclerVerticale = binding.RecyclerViewVerticaleH

        titoloFinestra= binding.testoHotel

        testoTutti= binding.testoTuttiH
        testoCategoria= binding.testoCategoriaH
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