package com.example.pocketjourney

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketjourney.databinding.FragmentUpBarBinding


class UpBarFragment : Fragment() {
    private val TAG = "UpBarFragment"
    private lateinit var binding: FragmentUpBarBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpBarBinding.inflate(inflater)

        binding.optionButton.setOnClickListener{
            Log.i(TAG, "Bottone cliccato")
        }

        binding.profileButton.setOnClickListener{
            Log.i(TAG, "Bottone cliccato")
        }

        return binding.root
    }

}
