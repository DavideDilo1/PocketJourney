package com.example.pocketjourney.home.sezioniHome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
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
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        Log.e("ATTENZIONEEEEE","HA APERTO i consigli" + idUtente)
        requireActivity().intent.putExtra("frame","FrameConsigliati")

        binding.backArrowC.setOnClickListener(){
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.FrameConsigliati, HomeFragmentNew()).remove(this)
                .addToBackStack(null)
                .commit()
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spnCosaFare.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                // Aggiorna il terzo Spinner in base alla selezione del primo Spinner
                updateThirdSpinner(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.spnCitta.setPrompt("Scegli cosa fare")
            }
        })


        binding.spnPaese.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                // Aggiorna il terzo Spinner in base alla selezione del primo Spinner
                updateCittaSpinner(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.spnCitta.setPrompt("Città")
            }
        })

        binding.cercaMetaButton.setOnClickListener {

            val categoria = binding.spnCosaFare.selectedItem.toString()
            val tipologia = binding.spinnerTipoAttivita.selectedItem.toString()
            val paese = binding.spnPaese.selectedItem.toString()
            val citta = binding.spnCitta.selectedItem.toString()
            val idUtente = requireActivity().intent.getStringExtra("idUtente")
            val manager=requireActivity().supportFragmentManager
            Log.d("hai scelto", categoria + tipologia + paese + citta)
            val childFragment = PaginaConsigliatiFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            requireActivity().intent.putExtra("categoria",categoria)
            requireActivity().intent.putExtra("tipologia",tipologia)
            requireActivity().intent.putExtra("paese",paese)
            requireActivity().intent.putExtra("citta",citta)

            manager.beginTransaction().replace(R.id.FrameConsigliati, childFragment)
            .addToBackStack(null)
            .commit()
        }

    }

    private fun updateThirdSpinner(position: Int) {
        val soggiorniList: List<String> = resources.getStringArray(R.array.soggiorni).toList()
        val ristorantiList: List<String> = resources.getStringArray(R.array.ristoranti).toList()
        val attrazioniList: List<String> = resources.getStringArray(R.array.attrazioni).toList()

        val thirdSpinnerData: Array<String> = when (position) {
            0 -> ristorantiList.toTypedArray() // Selezionato "Andare a mangiare"
            1 -> attrazioniList.toTypedArray() // Selezionato "Fare qualcosa di divertente"
            2 -> soggiorniList.toTypedArray() // Selezionato "Soggiornare"
            else -> arrayOf() // Nessun dato
        }

        val thirdSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, thirdSpinnerData)
        binding.spinnerTipoAttivita.adapter = thirdSpinnerAdapter
    }

    private fun updateCittaSpinner(position: Int) {
        val brasileCittaList: List<String> = resources.getStringArray(R.array.brasile_citta).toList()
        val canadaCittaList: List<String> = resources.getStringArray(R.array.canada_citta).toList()
        val cinaCittaList: List<String> = resources.getStringArray(R.array.cina_citta).toList()
        val franciaCittaList: List<String> = resources.getStringArray(R.array.francia_citta).toList()
        val germaniaCittaList: List<String> = resources.getStringArray(R.array.germania_citta).toList()
        val giapponeCittaList: List<String> = resources.getStringArray(R.array.giappone_citta).toList()
        val greciaCittaList: List<String> = resources.getStringArray(R.array.grecia_citta).toList()
        val irlandaCittaList: List<String> = resources.getStringArray(R.array.irlanda_citta).toList()
        val italiaCittaList: List<String> = resources.getStringArray(R.array.italia_citta).toList()
        val paesiBassiCittaList: List<String> = resources.getStringArray(R.array.paesibassi_citta).toList()
        val regnoUnitoCittaList: List<String> = resources.getStringArray(R.array.regnounito_citta).toList()
        val spagnaCittaList: List<String> = resources.getStringArray(R.array.spagna_citta).toList()
        val statiUnitiCittaList: List<String> = resources.getStringArray(R.array.statiuniti_citta).toList()
        val tailandiaCittaList: List<String> = resources.getStringArray(R.array.tailandia_citta).toList()
        val cittaList: List<String> = when (position) {
            0 -> brasileCittaList
            1 -> canadaCittaList
            2 -> cinaCittaList
            3 -> franciaCittaList
            4 -> germaniaCittaList
            5 -> giapponeCittaList
            6 -> greciaCittaList
            7 -> irlandaCittaList
            8 -> italiaCittaList
            9 -> paesiBassiCittaList
            10 -> regnoUnitoCittaList
            11 -> spagnaCittaList
            12 -> statiUnitiCittaList
            13 -> tailandiaCittaList
            else -> emptyList() // Nessuna lista disponibile
        }

        // Aggiorna gli elementi del secondo Spinner con i nuovi dati
        val cittaSpinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cittaList)
        binding.spnCitta.adapter = cittaSpinnerAdapter
    }
}