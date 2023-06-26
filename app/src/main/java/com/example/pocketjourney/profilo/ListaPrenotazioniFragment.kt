package com.example.pocketjourney.profilo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.ProfileFragment
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.PrenotazioniAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DbHelper
import com.example.pocketjourney.databinding.FragmentListaPrenotazioniBinding
import com.example.pocketjourney.model.PrenotazioniModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaPrenotazioniFragment : Fragment() {
    private lateinit var binding: FragmentListaPrenotazioniBinding
    private lateinit var databaseHelper: DbHelper
    private lateinit var bookRecycle: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentListaPrenotazioniBinding.inflate(layoutInflater,container,false)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val emailUtente=requireActivity().intent.getStringExtra("emailUtenteOffline")
        Log.d("dati ricevuti ", idUtente + emailUtente)
        // Inflate the layout for this fragment

        binding.recyclerPrenotazioni.layoutManager = LinearLayoutManager(requireContext())

        if (idUtente != null) {
            setRecyclerView(idUtente)
        } else {
            if (emailUtente != null) {
                setRecyclerView(emailUtente)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backArrowR.setOnClickListener {
            val childFragment = ProfileFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.FrameListaPrenotazioni, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun setRecyclerView(idUtente: String) {
        //in questo metodo uso id utente ma nel caso delle funzionalita in locale la variabile contiene la email
        val bookItem = ArrayList<PrenotazioniModel>()
        val bookAdapter = PrenotazioniAdapter(bookItem)
        //imposto adapter sulla recycler view
        binding.recyclerPrenotazioni.adapter=bookAdapter

        val queryPopolazionePrenotazioni= "SELECT * FROM Prenotazioni JOIN Posti WHERE Prenotazioni.ref_utente = '${idUtente}' and Prenotazioni.ref_posto = Posti.idPosti"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazionePrenotazioni)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    Log.d("JSON", response.body().toString())
                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset per la popolazione delle prenotazioni" )
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size()>0){
                            Log.e("Ciao", "sto per entrare nel for")
                            for(i in querySetArray){
                                Log.d("RISULTATO DELLA QUERY PER LA POPOLAZIONE", i.toString())
                                val elemento= i as JsonObject
                                bookItem.add(
                                    //inserisco id,nome,data,numpersone e orario
                                    PrenotazioniModel(
                                        elemento.get("idPrenotazioni").asInt,
                                        elemento.get("nome").toString(),
                                        elemento.get("data").toString(),
                                        elemento.get("numPersone").toString(),
                                        elemento.get("orario").toString()
                                    )
                                )
                                bookAdapter.notifyDataSetChanged()
                            }


                            //configuriamo l'adapter con la recycler view
                            binding.recyclerPrenotazioni.adapter = bookAdapter

                        }
                    }


                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
                // Popolare dalla tabella Prenotazioni locale
                val dbHelper = DbHelper(requireContext())
                val dataList = dbHelper.getAllPrenotazioni(idUtente)

                // Aggiungere gli elementi alla lista dell'adapter
                bookItem.addAll(dataList)

                // Notificare all'adapter che i dati sono cambiati
                bookAdapter.notifyDataSetChanged()

                Log.e("ciao", t.toString() + " " + t.message.toString())
            }

        })
    }

}