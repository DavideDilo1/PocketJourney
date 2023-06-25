package com.example.pocketjourney.home

import java.text.SimpleDateFormat
import java.util.Locale
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.pocketjourney.R
import com.example.pocketjourney.databinding.FragmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*

class BookingFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentBookingBinding
    private lateinit var spinnerPersone: Spinner
    private lateinit var adapterPersone: ArrayAdapter<CharSequence>
    private lateinit var dataSelezionataText: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBookingBinding.inflate(inflater)

        dataSelezionataText = binding.dataSelezionata

        spinnerPersone = binding.personSpinner
        adapterPersone= ArrayAdapter.createFromResource(requireContext(), R.array.numero_persone, android.R.layout.simple_spinner_item )
        adapterPersone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPersone.adapter = adapterPersone

        spinnerPersone.onItemSelectedListener = this




        binding.buttonPrenota.setOnClickListener{

            Toast.makeText(requireContext(), "Prenotazione avvenuta con successo", Toast.LENGTH_SHORT ).show()
        }



        dataSelezionataText.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Seleziona la data")
                .build()


            picker.show(parentFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                dataSelezionataText.text = convertTimeToDate(it.first) + " - " + convertTimeToDate(it.second)
            }


            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }

        }




        return binding.root
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun convertTimeToDate(time: Long): String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(utc.time)
    }
}


