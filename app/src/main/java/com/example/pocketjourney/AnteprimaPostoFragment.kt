package com.example.pocketjourney

import android.annotation.TargetApi
import androidx.core.util.Pair
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import com.example.pocketjourney.databinding.FragmentAnteprimaPostoBinding
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem

class AnteprimaPostoFragment : Fragment() {

    private lateinit var binding: FragmentAnteprimaPostoBinding


    private lateinit var second_back_arrow: ImageView
    private lateinit var second_arrow_up: ImageView
    private lateinit var second_title: TextView
    private lateinit var second_subtitle: TextView
    private lateinit var second_rating_number: TextView
    private lateinit var second_rating_number2: TextView
    private lateinit var more_details: TextView
    private lateinit var second_ratingbar: RatingBar

    private lateinit var from_left: Animation
    private lateinit var from_right: Animation
    private lateinit var from_bottom: Animation

    @TargetApi(Build.VERSION_CODES.S)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAnteprimaPostoBinding.inflate(inflater)

        val home = requireArguments().getParcelable<HomeItemModel>("home")

        val ristoranti = requireArguments().getParcelable<HorizontalRecyclerItem>("ristoranti")


        if(home != null){
            Log.i("Home", "ALmeno qua ci entro?")

            val titolo : TextView = binding.secondTitle
            val background_image: ImageView = binding.imageBackgroundAnteprima
            val rating: RatingBar = binding.secondRatingBar
            val media_recensioni : TextView = binding.secondRatingNumber
            val num_recensioni : TextView = binding.secondRatingNumber2

            val descrizione : TextView = binding.secondSubtitle


            // sets the text to the textview from our itemHolder class


            titolo.text = home.title
            background_image.setImageResource(home.image)
            rating.rating = home.stelle
            media_recensioni.text = home.valutazione
            num_recensioni.text = home.numRec

            descrizione.text = "Query al dbms"


        }
        /*
        else{
            //TODO: SCHERMATA NESSUN DATO DISPONIBILE
        }*/

        if(ristoranti !=null ){
            Log.i("Ristoranti", "ALmeno qua ci entro?")
            val titolo : TextView = binding.secondTitle
            val background_image: ImageView = binding.imageBackgroundAnteprima
            val rating: RatingBar = binding.secondRatingBar
            val media_recensioni : TextView = binding.secondRatingNumber
            val num_recensioni : TextView = binding.secondRatingNumber2

            val descrizione : TextView = binding.secondSubtitle

            titolo.text = ristoranti.nomeRistorante
            background_image.setImageResource(ristoranti.imageUrl)
            rating.rating = ristoranti.stelle
            media_recensioni.text = ristoranti.valutazione
            num_recensioni.text = ristoranti.numRec

            descrizione.text = "Query al dbms"



        }



        second_arrow_up = binding.secondArrowUp
        second_back_arrow = binding.secondBackArrow
        second_title = binding.secondTitle
        second_subtitle = binding.secondSubtitle
        second_rating_number = binding.secondRatingNumber
        second_ratingbar = binding.secondRatingBar
        more_details = binding.moreDetails
        second_rating_number2 = binding.secondRatingNumber2

        second_back_arrow.setOnClickListener(){
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_anteprima_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        requireActivity().window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }


        from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)
        from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)

        second_back_arrow.setAnimation(from_left)
        second_title.setAnimation(from_right)
        second_subtitle.setAnimation(from_right)
        second_ratingbar.setAnimation(from_left)
        second_rating_number.setAnimation(from_right)
        second_rating_number2.setAnimation(from_left)
        second_arrow_up.setAnimation(from_bottom)
        more_details.setAnimation(from_bottom)

        second_arrow_up.setOnClickListener{
            val pairs = arrayOf<Pair<View, String>>(
                Pair(second_arrow_up, "background_image_transition")
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), *pairs).toBundle()

            val childFragment = PaginaPostoFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            childFragment.arguments = options
            fragmentTransaction.replace(R.id.fragment_anteprima_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }






        return binding.root;
    }


}

