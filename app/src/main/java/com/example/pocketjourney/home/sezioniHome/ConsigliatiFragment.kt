package com.example.pocketjourney.home.sezioniHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentConsigliatiBinding
import com.example.pocketjourney.home.HomeFragmentNew

class ConsigliatiFragment : Fragment() {

    private lateinit var binding: FragmentConsigliatiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConsigliatiBinding.inflate(inflater)



        binding.backArrow.setOnClickListener(){
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FrameConsigliati, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }


        return binding.root
    }


}