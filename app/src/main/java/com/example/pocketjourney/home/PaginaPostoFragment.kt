package com.example.pocketjourney.home

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ScrollView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentPaginaPostoBinding

class PaginaPostoFragment : Fragment() {
    private lateinit var binding: FragmentPaginaPostoBinding

    private lateinit var down_arrow: ImageView
    private lateinit var third_scrollView: ScrollView
    private lateinit var from_bottom: Animation


    @TargetApi(Build.VERSION_CODES.S)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaginaPostoBinding.inflate(inflater)

        down_arrow = binding.downArrow
        third_scrollView = binding.thirdScrollView

        from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)

        down_arrow.setAnimation(from_bottom)
        third_scrollView.setAnimation(from_bottom)


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
                    )
        }

        down_arrow.setOnClickListener{
            val pairs = arrayOf<Pair<View, String>>(
                Pair(down_arrow, "background_image_transition")
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), *pairs).toBundle()

            val childFragment = AnteprimaPostoFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            childFragment.arguments = options
            fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        binding.prenotaAdessoButton.setOnClickListener{

            val childFragment = BookingFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }





        return binding.root
    }

}