package com.example.pocketjourney

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.pocketjourney.databinding.FragmentHomeNewBinding
import android.os.Build.VERSION_CODES.S
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton


class HomeFragmentNew : Fragment() {

    private lateinit var binding: FragmentHomeNewBinding

    private lateinit var cardView: CardView
    private lateinit var cardView2: CardView
    private lateinit var cardView3: CardView
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView

    private lateinit var searchView: SearchView
    private lateinit var favoriteButton1: ToggleButton
    private lateinit var favoriteButton2: ToggleButton
    private lateinit var favoriteButton3: ToggleButton
    private lateinit var ristorantiButton: Button
    private lateinit var hotelButton: Button
    private lateinit var attrazioniButton: Button
    private lateinit var home_background: ImageView
    private lateinit var ideaButton: ImageButton

    private lateinit var anim_from_button: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation

    @TargetApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeNewBinding.inflate(inflater)


        cardView = binding.cardView
        cardView2 = binding.cardView2
        cardView3 = binding.cardView3

        home_background = binding.imageBackground
        ristorantiButton = binding.resturantButton
        attrazioniButton = binding.attractionButton
        hotelButton = binding.hotelButton

        textView = binding.testoCosaStaiCercando
        textView2 = binding.testoHome
        textView3 = binding.testoMiglioriMete
        searchView = binding.searchViewHome

        ideaButton = binding.ideaButton


        //load animations
        anim_from_button = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        anim_from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)
        //set animations

        cardView.setAnimation(anim_from_button)
        cardView2.setAnimation(anim_from_button)
        cardView3.setAnimation(anim_from_button)
        textView.setAnimation(anim_from_top)
        textView2.setAnimation(anim_from_top)
        textView3.setAnimation(anim_from_button)
        ideaButton.setAnimation(anim_from_right)

        searchView.setAnimation(anim_from_left)

        home_background.setAnimation(anim_from_top)
        ristorantiButton.setAnimation(anim_from_left)
        attrazioniButton.setAnimation(anim_from_left)
        hotelButton.setAnimation(anim_from_left)

        cardView.setOnClickListener(View.OnClickListener { view ->
            val childFragment = Fragment1()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        })

        favoriteButton1 = binding.FavoriteButton
        favoriteButton2 = binding.FavoriteButton2
        favoriteButton3 = binding.FavoriteButton3

        favoriteButton1.setOnClickListener {

            if(favoriteButton1.isChecked()){
                //TODO: se cliccato aggiungi ai preferiti
                Toast.makeText(requireContext(), "Aggiunto ai Preferiti", Toast.LENGTH_SHORT).show()
            }else{
                //TODO: se non cliccato rimuovi dai preferiti

            }
        }

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



