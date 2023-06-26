package com.example.pocketjourney.home.sezioniHome

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentPacchettiBinding
import com.example.pocketjourney.home.HomeFragmentNew

class PacchettiFragment : Fragment() {

    private lateinit var binding: FragmentPacchettiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPacchettiBinding.inflate(inflater)


        binding.backArrowP.setOnClickListener {
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.framePacchetti, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return binding.root
    }

}