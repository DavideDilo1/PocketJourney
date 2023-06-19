package com.example.pocketjourney

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.example.pocketjourney.databinding.FragmentHomeNewBinding
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.adapter.HomeAdapter
import com.example.pocketjourney.model.HomeItemModel


class HomeFragmentNew : Fragment() {

    private lateinit var binding: FragmentHomeNewBinding


/*
    private lateinit var cardView: CardView
    private lateinit var cardView2: CardView
    private lateinit var cardView3: CardView*/

    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView

    private lateinit var homeRecycle: RecyclerView

    private lateinit var searchView: SearchView
    private lateinit var favoriteButton1: ToggleButton
    private lateinit var favoriteButton2: ToggleButton
    private lateinit var favoriteButton3: ToggleButton
    private lateinit var ristorantiButton: Button
    private lateinit var hotelButton: Button
    private lateinit var attrazioniButton: Button
    private lateinit var home_background: ImageView
    private lateinit var ideaButton: ImageButton

    private lateinit var anim_from_bottom: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation

    @TargetApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeNewBinding.inflate(inflater)

        //definiamo la recycler view della home:

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        //arraylist che contiene gli elementi della recycler

        val homeItem = ArrayList<HomeItemModel>()
        //un ITEM VIEW MODEL é FATTO: val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float

        homeItem.add(HomeItemModel(R.drawable.image_one, "The START Hotel, Casino &amp; SkyPod", "(510)", "4.91", 4.5F ))
        homeItem.add(HomeItemModel(R.drawable.image_two, "Bar di economia", "(510)", "4.91", 4.5F ))
        homeItem.add(HomeItemModel(R.drawable.image_three, "Bar di architettura", "(510)", "4.91", 4.5F ))

        //passiamo l'array list all'adapter:

        val homeAdapter = HomeAdapter(homeItem)

        //configuriamo l'adapter con la recycler view
        binding.homeRecyclerView.adapter = homeAdapter


        homeAdapter.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("home", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameNewHomeLayout, childFragment)
                .addToBackStack(null)
                .commit()

        }

       /*  onclick listener su un elemento della recycler.


       adapter.setOnClickListener(object:
            HomeAdapter.OnClickListener {
                override fun onClick(position: Int, model: HomeItemModel){
                    val childFragment = Fragment1()
                    val fragmentTransaction = childFragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

            }
        )


       */

        // cardView = binding.cardView
        // cardView2 = binding.cardView2
        // cardView3 = binding.cardView3


        //TODO: aggiungere le animazioni alla recycler view

        home_background = binding.imageBackground
        ristorantiButton = binding.resturantButton
        attrazioniButton = binding.attractionButton
        hotelButton = binding.hotelButton

        textView = binding.testoCosaStaiCercando
        textView2 = binding.testoHome
        textView3 = binding.testoMiglioriMete
        searchView = binding.searchViewHome

        ideaButton = binding.ideaButton

        homeRecycle = binding.homeRecyclerView

        //load animations
        anim_from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        anim_from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)
        //set animations

        //cardView.setAnimation(anim_from_button)
     //   cardView2.setAnimation(anim_from_button)
      //  cardView3.setAnimation(anim_from_button)
        textView.setAnimation(anim_from_top)
        textView2.setAnimation(anim_from_top)
        textView3.setAnimation(anim_from_bottom)
        ideaButton.setAnimation(anim_from_right)

        searchView.setAnimation(anim_from_left)

        home_background.setAnimation(anim_from_top)
        ristorantiButton.setAnimation(anim_from_left)
        attrazioniButton.setAnimation(anim_from_left)
        hotelButton.setAnimation(anim_from_left)


        homeRecycle.setAnimation(anim_from_bottom)


       // favoriteButton1 = binding.FavoriteButton
      //  favoriteButton2 = binding.FavoriteButton2
      //  favoriteButton3 = binding.FavoriteButton3

/*
        favoriteButton1.setOnClickListener {

            if(favoriteButton1.isChecked()){
                //TODO: se cliccato aggiungi ai preferiti
                Toast.makeText(requireContext(), "Aggiunto ai Preferiti", Toast.LENGTH_SHORT).show()
            }else{
                //TODO: se non cliccato rimuovi dai preferiti

            }
        }*/

        ideaButton.setOnClickListener{
            //TODO: implementare le schermate idea
        }

        ristorantiButton.setOnClickListener(View.OnClickListener { view ->
            val childFragment = RistorantiFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        })

        hotelButton.setOnClickListener{
            val childFragment = HotelFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        attrazioniButton.setOnClickListener{
            val childFragment = AttrazioniFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        return binding.root
    }




}



