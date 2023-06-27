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
import com.example.pocketjourney.databinding.FragmentAttrazioniBinding
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem


class AttrazioniFragment : Fragment() {

    private lateinit var binding: FragmentAttrazioniBinding

    private lateinit var back_arrowA: ImageView

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
        binding = FragmentAttrazioniBinding.inflate(inflater)

        binding.RecyclerViewOrizzontaleA.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        val attrazioniItemList1 = ArrayList<HorizontalRecyclerItem>()



        val ristorantiOrizzAdapter1 = HorizontalItemAdapter(attrazioniItemList1)
        binding.RecyclerViewOrizzontaleA.adapter = ristorantiOrizzAdapter1


        ristorantiOrizzAdapter1.onItemClick = {

            val bundle = Bundle()

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle
            Log.i("Attrazioni" , "ALmeno qua ci entro e creo il fragment?")

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameAttrazioni, childFragment)
                .addToBackStack(null)
                .commit()

        }


        val allTypeAttrazioni = ArrayList<HomeItemModel>()
        //un ITEM VIEW MODEL é FATTO: val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float



        binding.RecyclerViewVerticaleA.layoutManager = LinearLayoutManager(requireContext())

        val attrazioniAdapter = HomeAdapter(allTypeAttrazioni)
        binding.RecyclerViewVerticaleA.adapter = attrazioniAdapter






        back_arrowA= binding.backArrowA

        back_arrowA.setOnClickListener(){
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameAttrazioni, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        topImage = binding.imageBackground
        recyclerOrizzontale = binding.RecyclerViewOrizzontaleA

        recyclerVerticale = binding.RecyclerViewVerticaleA

        titoloFinestra= binding.testoAttrazioni

        testoTutti= binding.testoTuttiA
        testoCategoria= binding.testoCategoriaA
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