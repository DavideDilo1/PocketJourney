package com.example.pocketjourney.home.sezioniHome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.home.AnteprimaPostoFragment
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.HomeAdapter
import com.example.pocketjourney.adapter.HorizontalItemAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentHotelBinding
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HotelFragment : Fragment() {

    private lateinit var binding: FragmentHotelBinding
    private lateinit var topImage:ImageView
    private lateinit var recyclerOrizzontale: RecyclerView
    private lateinit var recyclerVerticale: RecyclerView
    private lateinit var titoloFinestra: TextView
    private lateinit var testoTutti: TextView
    private lateinit var testoCategoria: TextView
    private lateinit var searchView: SearchView
    private var homeItem = ArrayList<HomeItemModel>()
    private var homeAdapter = HomeAdapter(homeItem)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHotelBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        requireActivity().intent.putExtra("frame","frameHotel")

        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
            setRecyclerViewOrizzontale(idUtente.toInt())
        }

        binding.RecyclerViewOrizzontaleH.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerViewVerticaleH.layoutManager = LinearLayoutManager(requireContext())

        binding.backArrowH.setOnClickListener(){
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameHotel, HomeFragmentNew())
                .addToBackStack(null)
                .commit()
        }


        topImage = binding.imageBackground
        recyclerOrizzontale = binding.RecyclerViewOrizzontaleH

        recyclerVerticale = binding.RecyclerViewVerticaleH

        titoloFinestra= binding.testoHotel

        testoTutti= binding.testoTuttiH
        testoCategoria= binding.testoCategoriaH
        searchView= binding.searchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterList(newText)
                }

                return true
            }


        })



        return binding.root
    }

    private fun setRecyclerView(idUtente: Int) {

        val scope = CoroutineScope(Dispatchers.Default)
        //imposto adapter sulla recycler view
        binding.RecyclerViewVerticaleH.adapter=homeAdapter
        scope.launch {
            val queryPopolazioneLista =
                "SELECT idPosti,nome,valutazione,numRecensioni,foto FROM Posti WHERE categoria='Soggiorno'"
            val userAPI = ClientNetwork.retrofit
            val call = userAPI.cerca(queryPopolazioneLista)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset")) {

                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")

                            if (querySetArray != null && querySetArray.size() > 0) {

                                for (i in querySetArray) {
                                    var bitmap: Bitmap
                                    val elemento = i as JsonObject
                                    val foto = elemento.get("foto").asString
                                    val downloadFotoPosto = userAPI.getAvatar(foto)
                                    downloadFotoPosto.enqueue(object : Callback<ResponseBody> {
                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                                        ) {

                                            if (response.isSuccessful) {

                                                val responseBody = response.body()
                                                if (responseBody != null) {
                                                    val inputStream = responseBody.byteStream()
                                                    bitmap = BitmapFactory.decodeStream(inputStream)
                                                    homeItem.add(
                                                        HomeItemModel(
                                                            elemento.get("idPosti").asInt,
                                                            bitmap,
                                                            elemento.get("nome").toString(),
                                                            elemento.get("numRecensioni")
                                                                .toString(),
                                                            elemento.get("valutazione").toString()
                                                        )
                                                    )
                                                    // Aggiorna l'adapter dopo aver aggiunto l'elemento
                                                    homeAdapter.notifyDataSetChanged()
                                                }
                                                homeAdapter.setOnItemClickListener { homeItemModel ->
                                                    val id = homeItemModel.id
                                                    val childFragment = AnteprimaPostoFragment()
                                                    requireActivity().intent.putExtra(
                                                        "idPosto",
                                                        id.toString()
                                                    )
                                                    requireActivity().intent.putExtra(
                                                        "idUtente",
                                                        idUtente.toString()
                                                    )
                                                    requireActivity().intent.putExtra(
                                                        "provenienza",
                                                        "hotelFragment"
                                                    )
                                                    val fragmentTransaction =
                                                        requireActivity().supportFragmentManager.beginTransaction()
                                                    fragmentTransaction.replace(
                                                        R.id.frameNewHomeLayout,
                                                        childFragment
                                                    )
                                                    fragmentTransaction.addToBackStack(null)
                                                    fragmentTransaction.commit()
                                                }

                                                homeAdapter.setOnToggleClickListener { homeItemModel, isChecked ->
                                                    if (isChecked) {
                                                        // Il toggle è stato selezionato
                                                        val idPosto = homeItemModel.id
                                                        setPreferiti(idPosto, idUtente)
                                                        // ... altre azioni da eseguire
                                                    } else {
                                                        // Il toggle è stato deselezionato
                                                        val idPosto = homeItemModel.id
                                                        rimuoviPreferiti(idPosto, idUtente)
                                                    }
                                                }

                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseBody>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                requireContext(),
                                                "L'immagine non è stata trovata correttamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })

                                }


                                //configuriamo l'adapter con la recycler view
                                binding.RecyclerViewVerticaleH.adapter = homeAdapter

                            }
                        }


                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                }
            })
        }
    }



    private fun setRecyclerViewOrizzontale(idUtente: Int) {
        val horizontalItem = ArrayList<HorizontalRecyclerItem>()
        val horizontalAdapter = HorizontalItemAdapter(horizontalItem)
        val scope = CoroutineScope(Dispatchers.Default)
        //imposto adapter sulla recycler view
        binding.RecyclerViewOrizzontaleH.adapter=horizontalAdapter

        val queryPopolazioneTop5= "SELECT idPosti,nome,valutazione,numRecensioni,foto,descrizione FROM Posti WHERE categoria='Soggiorno' ORDER BY valutazione DESC  LIMIT 5"
        scope.launch {
            val userAPI = ClientNetwork.retrofit
            val call = userAPI.cerca(queryPopolazioneTop5)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset")) {
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size() > 0) {
                                for (i in querySetArray) {
                                    var bitmap: Bitmap
                                    val elemento = i as JsonObject
                                    val foto = elemento.get("foto").asString
                                    val downloadFotoPosto = userAPI.getAvatar(foto)
                                    downloadFotoPosto.enqueue(object : Callback<ResponseBody> {
                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                                        ) {
                                            if (response.isSuccessful) {
                                                val responseBody = response.body()
                                                if (responseBody != null) {
                                                    val inputStream = responseBody.byteStream()
                                                    bitmap = BitmapFactory.decodeStream(inputStream)
                                                    horizontalItem.add(
                                                        HorizontalRecyclerItem(
                                                            elemento.get("idPosti").asInt,
                                                            bitmap,
                                                            elemento.get("nome").toString(),
                                                            elemento.get("numRecensioni")
                                                                .toString(),
                                                            elemento.get("valutazione").toString(),
                                                            elemento.get("descrizione").toString()
                                                        )
                                                    )
                                                    // Aggiorna l'adapter dopo aver aggiunto l'elemento
                                                    horizontalAdapter.notifyDataSetChanged()
                                                }
                                                horizontalAdapter.setOnItemClickListener { horizontalRecyclerItem ->
                                                    val id = horizontalRecyclerItem.id
                                                    val childFragment = AnteprimaPostoFragment()
                                                    requireActivity().intent.putExtra(
                                                        "idPosto",
                                                        id.toString()
                                                    )
                                                    requireActivity().intent.putExtra(
                                                        "idUtente",
                                                        idUtente.toString()
                                                    )
                                                    requireActivity().intent.putExtra(
                                                        "provenienza",
                                                        "hotelFragment"
                                                    )
                                                    val fragmentTransaction =
                                                        requireActivity().supportFragmentManager.beginTransaction()
                                                    fragmentTransaction.replace(
                                                        R.id.frameNewHomeLayout,
                                                        childFragment
                                                    )
                                                    fragmentTransaction.addToBackStack(null)
                                                    fragmentTransaction.commit()
                                                }


                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseBody>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                requireContext(),
                                                "L'immagine non è stata trovata correttamente",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })

                                }


                                //configuriamo l'adapter con la recycler view
                                binding.RecyclerViewOrizzontaleH.adapter = horizontalAdapter

                            }
                        }


                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                }
            })
        }
    }

    private fun rimuoviPreferiti(idPosto: Int, idUtente: Int) {
        val scope = CoroutineScope(Dispatchers.Default)
        val queryRimuoviFav= "DELETE FROM Preferiti WHERE ref_utente = '${idUtente}' AND ref_posto = '${idPosto}'"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.remove(queryRimuoviFav)
        scope.launch{
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "Hai rimosso l'elemento dai preferiti!", Toast.LENGTH_SHORT).show()
                    }
                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                }
            })
        }
    }

    private fun setPreferiti(idPosto: Int,idUtente: Int) {
        val scope = CoroutineScope(Dispatchers.Default)
        //controllo se l'elemento è già presente nei preferiti
        val queryControlloFav= "SELECT * FROM Preferiti WHERE ref_utente='${idUtente}' and ref_posto='${idPosto}'"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryControlloFav)
        scope.launch{
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                //ho gia il posto nei pref
                                Toast.makeText(requireContext(), "Hai già inserito questo luogo tra i preferiti!", Toast.LENGTH_SHORT).show()
                            } else {
                                //il posto non c'è posso fare query
                                val queryInserisciFav= "INSERT INTO Preferiti (ref_utente, ref_posto) VALUES ('${idUtente}','${idPosto}')"
                                val call = userAPI.inserisci(queryInserisciFav)
                                call.enqueue(object : Callback<JsonObject> {
                                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                        if (response.isSuccessful) {
                                            // L'inserimento dell'utente online è avvenuto con successo e lo inserisco in locale
                                            Toast.makeText(requireContext(), "Inserimento nei preferiti avvenuto!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                    }
                                })

                            }

                        }
                    }


                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                }
            })
        }
    }

    private fun filterList(query : String){
        if( query != null){
            val filteredList = ArrayList<HomeItemModel>()
            for(i in homeItem ){
                if(i.title.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
                //se la vista è vuota<<'
                Toast.makeText(requireContext(), "Nessun dato trovato", Toast.LENGTH_SHORT)
            }else{
                homeAdapter.setFilteredList(filteredList)
            }



        }

    }

}