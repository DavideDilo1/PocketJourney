package com.example.pocketjourney.home

import java.text.SimpleDateFormat
import java.util.Locale
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.JsonObject
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BookingFragment : Fragment(){

    private lateinit var binding: FragmentBookingBinding
    private lateinit var spinnerPersone: Spinner
    private lateinit var dataSelezionataText: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBookingBinding.inflate(layoutInflater,container,false)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val idPosto= requireActivity().intent.getStringExtra("idPosto")
        val categoria = requireActivity().intent.getStringExtra("categoria")
        Log.d("sono prenotazione ho ricevuto",idUtente + " " + idPosto + "" + categoria)
        dataSelezionataText = binding.dataSelezionata
        dataSelezionataText.setOnClickListener {
            //CALENDARIO DATA RANGE:
           if(categoria=="Soggiorno") {
               Log.e("MOSTRO IL CALENDARIO","PER HOTEL ")
               val picker = MaterialDatePicker.Builder.dateRangePicker()
                   .setTheme(R.style.ThemeMaterialCalendar)
                   .setTitleText("Seleziona la data")
                   .build()
               picker.show(parentFragmentManager, "TAG")
               picker.addOnPositiveButtonClickListener {
                   dataSelezionataText.text =
                       convertTimeToDate(it.first) + " - " + convertTimeToDate(it.second)
               }
               picker.addOnNegativeButtonClickListener {
                   picker.dismiss()
               }
           }

            else if (categoria=="Ristorante" || categoria=="Attrazione"){
               //CALENDARIO SINGOLO
               Log.e("MOSTRO IL CALENDARIO","SINGOLO")
               val picker = MaterialDatePicker.Builder.datePicker()
                   .setTheme(R.style.ThemeMaterialCalendar)
                   .setTitleText("Seleziona una data")
                   .build()

               picker.show(parentFragmentManager, "TAG")

               picker.addOnPositiveButtonClickListener {
                   dataSelezionataText.text = convertTimeToDate(it)
               }

               picker.addOnNegativeButtonClickListener {
                   picker.dismiss()
               }


            }

        }






        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonPrenota.setOnClickListener{

            val categoria = requireActivity().intent.getStringExtra("categoria")
            val idUtente = requireActivity().intent.getStringExtra("idUtente")
            val idPosto= requireActivity().intent.getStringExtra("idPosto")

            val dataPrenotazione=dataSelezionataText.text.toString()
            Log.d("data=", dataPrenotazione)
            val numPersone=binding.personSpinner.selectedItem.toString()
            val ora=binding.spinnerOrario.selectedItem.toString()
            Log.d("data e ora=", numPersone + " " + ora)

            if (categoria=="Ristorante" || categoria=="Attrazione"){
                //sto prenotando una data singola
                if(verificaDataSingola(dataPrenotazione)){
                    //la data corretta posso fare la query (una fun dopo a cui passo i dati)

                }
            } else if (categoria=="Soggiorno"){
                //sto prenotando data doppia
                if(verificaDataDoppia(dataPrenotazione)){
                    //la data corretta posso fare la query (una fun dopo a cui passo i dati)

                }
            }



            Toast.makeText(requireContext(), "Prenotazione avvenuta con successo", Toast.LENGTH_SHORT ).show()
        }






        binding.backArrowBok.setOnClickListener{
            val childFragment = PaginaPostoFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.booking_Layout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }



    private fun convertTimeToDate(time: Long): String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return format.format(utc.time)
    }
    fun verificaDataSingola(data: String): Boolean {
        val dataCorrente = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date1 = sdf.parse(data)
        val date2 = sdf.parse(dataCorrente)
        if(date1.compareTo(date2)==-1){
            return false
        }
        return true

    }

    fun verificaDataDoppia(data: String):Boolean{
        //
        return true
    }


}


