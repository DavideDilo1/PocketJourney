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
import android.widget.Toast
import android.widget.ToggleButton


class HomeFragmentNew : Fragment() {

    private lateinit var binding: FragmentHomeNewBinding

    private lateinit var cardView: CardView
    private lateinit var cardView2: CardView
    private lateinit var cardView3: CardView
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView
    private lateinit var textView4: TextView
    private lateinit var textView5: TextView
    private lateinit var searchView: SearchView
    private lateinit var favoriteButton1: ToggleButton
    private lateinit var favoriteButton2: ToggleButton
    private lateinit var favoriteButton3: ToggleButton

    private lateinit var anim_from_button: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation


    @TargetApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeNewBinding.inflate(inflater)


        cardView = binding.cardView
        cardView2 = binding.cardView2
        cardView3 = binding.cardView3
        imageView = binding.imageViewHome
        textView = binding.firstText
        textView2 = binding.textView
        textView3 = binding.textView2
        textView4 = binding.textView3
        textView5 = binding.testView4
        searchView = binding.searchViewHome

        //load animations
        anim_from_button = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)

        //set animations

        cardView.setAnimation(anim_from_button)
        cardView2.setAnimation(anim_from_button)
        cardView3.setAnimation(anim_from_button)
        imageView.setAnimation(anim_from_top)
        textView.setAnimation(anim_from_top)
        textView2.setAnimation(anim_from_top)
        textView3.setAnimation(anim_from_top)
        textView4.setAnimation(anim_from_top)
        textView5.setAnimation(anim_from_top)
        searchView.setAnimation(anim_from_left)

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


        return binding.root
    }




}



