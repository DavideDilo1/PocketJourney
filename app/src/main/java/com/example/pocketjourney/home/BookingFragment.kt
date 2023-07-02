package com.example.pocketjourney.home

import java.text.SimpleDateFormat
import java.util.Locale
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.pocketjourney.R
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DBManager
import com.example.pocketjourney.databinding.FragmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BookingFragment : Fragment(){

    private lateinit var binding: FragmentBookingBinding
    private lateinit var dataSelezionataText: TextView
    private var dbManager: DBManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBookingBinding.inflate(layoutInflater,container,false)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val idPosto= requireActivity().intent.getStringExtra("idPosto")
        val categoria = requireActivity().intent.getStringExtra("categoria")
        val nomePosto=requireActivity().intent.getStringExtra("nomePosto")
        binding.titoloHotelToBook.text=nomePosto
        requireActivity().intent.putExtra("frame","booking_Layout")
        dataSelezionataText = binding.dataSelezionata
        dataSelezionataText.setOnClickListener {
            //CALENDARIO DATA RANGE:
            if(categoria=="Soggiorno") {
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
            val numPersone=binding.personSpinner.selectedItem.toString()
            val ora=binding.spinnerOrario.selectedItem.toString()

            if (categoria=="Ristorante" || categoria=="Attrazione"){
                //sto prenotando una data singola
                if(verificaDataSingola(dataPrenotazione)){
                    //la data corretta posso fare la query (una fun dopo a cui passo i dati)
                    verificaCartaDiCredito(idUtente) { bool ->
                        if (bool) {
                            // L'utente ha la carta di credito
                            // Esegui la funzione di inserimento prenotazione
                            inserisciPrenotazione(idUtente, idPosto, dataPrenotazione, numPersone, ora)
                            Toast.makeText(requireContext(), "Prenotazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                        } else {
                            // L'utente non ha la carta di credito
                            Toast.makeText(requireContext(), "Utente senza carta di credito", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "Inserisci un giorno valido", Toast.LENGTH_SHORT ).show()
                }

            } else if (categoria=="Soggiorno"){
                //sto prenotando data doppia
                if(verificaDataDoppia(dataPrenotazione)){
                    //la data corretta posso fare la query (una fun dopo a cui passo i dati)
                    verificaCartaDiCredito(idUtente) { bool ->
                        if (bool) {
                            // L'utente ha la carta di credito
                            // Esegui la funzione di inserimento prenotazione
                            inserisciPrenotazione(idUtente, idPosto, dataPrenotazione, numPersone, ora)
                            Toast.makeText(requireContext(), "Prenotazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                        } else {
                            // L'utente non ha la carta di credito
                            Toast.makeText(requireContext(), "Utente senza carta di credito", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), "Inserisci un giorno valido", Toast.LENGTH_SHORT ).show()
                }
            }
        }






        binding.backArrowBok.setOnClickListener{
            val childFragment = PaginaPostoFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.booking_Layout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun inserisciPrenotazione(idUtente: String?, idPosto: String?, dataPrenotazione: String, numPersone: String, ora: String) {
        //dati utili per l'inserimetno in locale
        val email = requireActivity().intent.getStringExtra("email")
        val nomePosto = requireActivity().intent.getStringExtra("nomePosto")
        val scope = CoroutineScope(Dispatchers.Default)
        val userAPI = ClientNetwork.retrofit
        val queryinserisciPrenotazione =
            "INSERT INTO Prenotazioni (ref_utente, ref_posto, data, numPersone, orario) VALUES ('$idUtente','$idPosto','$dataPrenotazione','$numPersone','$ora')"
        val call = userAPI.inserisci(queryinserisciPrenotazione)
        scope.launch {
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    // L'inserimento della carta è avvenuto
                    dbManager = context?.let { it2 -> DBManager(it2) }
                    if (dbManager != null) {
                        dbManager?.open()
                        if (email != null && nomePosto != null) {
                            dbManager?.insertPrenotazione(
                                email,
                                nomePosto,
                                dataPrenotazione,
                                numPersone,
                                ora
                            )
                        }
                    }
                    Toast.makeText(
                        requireContext(),
                        "Prenotazione avvenuta con successo!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online

            }
        })
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
        val primoGiorno=data.take(10)
        val dataCorrente = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date1 = sdf.parse(primoGiorno)
        val date2 = sdf.parse(dataCorrente)
        if(date1.compareTo(date2)==-1){
            return false
        }
        return true
    }

    fun verificaCartaDiCredito(idUtente: String?, callback: (Boolean) -> Unit) {
        val userAPI = ClientNetwork.retrofit
        val queryCartaPresente = "SELECT idDatiPagamento FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
        val call = userAPI.cerca(queryCartaPresente)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    if (jsonObject != null && jsonObject.has("queryset")) {
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size() > 0) {
                            callback(true) // Chiamata al callback con il valore true
                        } else {
                            callback(false) // Chiamata al callback con il valore false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false) // Chiamata al callback con il valore false in caso di errore
            }
        })
    }




}


