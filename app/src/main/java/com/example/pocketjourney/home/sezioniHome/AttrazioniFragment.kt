package com.example.pocketjourney.home.sezioniHome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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
import com.example.pocketjourney.databinding.FragmentAttrazioniBinding
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


class AttrazioniFragment : Fragment() {

    private lateinit var binding: FragmentAttrazioniBinding

    private lateinit var topImage:ImageView

    private lateinit var recyclerOrizzontale: RecyclerView
    private lateinit var recyclerVerticale: RecyclerView

    private lateinit var titoloFinestra: TextView
    private lateinit var testoTutti: TextView
    private lateinit var testoCategoria: TextView
    private lateinit var searchView: SearchView


    private lateinit var anim_from_bottom: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAttrazioniBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        Log.e("ATTENZIONEEEEE","HA APERTO LA ristoranti fragment " + idUtente)
        requireActivity().intent.putExtra("frame","frameAttrazioni")

        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
            setRecyclerViewOrizzontale(idUtente.toInt())
        }

        binding.RecyclerViewOrizzontaleA.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerViewVerticaleA.layoutManager = LinearLayoutManager(requireContext())


        binding.backArrowA.setOnClickListener(){
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameAttrazioni, HomeFragmentNew())
                .addToBackStack(null)
                .commit()
        }

        topImage = binding.imageBackground
        recyclerOrizzontale = binding.RecyclerViewOrizzontaleA

        recyclerVerticale = binding.RecyclerViewVerticaleA

        titoloFinestra= binding.testoAttrazioni

        testoTutti= binding.testoTuttiA
        testoCategoria= binding.testoCategoriaA
        searchView= binding.searchView
/*

        anim_from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        anim_from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)


        topImage.animation = anim_from_top
        recyclerOrizzontale.animation =  anim_from_left
        recyclerVerticale.animation = anim_from_bottom
        testoTutti.animation = anim_from_left
        testoCategoria.animation = anim_from_left
        titoloFinestra.animation = anim_from_top
        searchView.animation = anim_from_right

*/

        return binding.root
    }

    private fun setRecyclerView(idUtente: Int) {
        val homeItem = ArrayList<HomeItemModel>()
        val homeAdapter = HomeAdapter(homeItem)
        val scope = CoroutineScope(Dispatchers.Default)
        //imposto adapter sulla recycler view
        binding.RecyclerViewVerticaleA.adapter = homeAdapter

        val queryPopolazioneLista =
            "SELECT idPosti,nome,valutazione,numRecensioni,foto FROM Posti WHERE categoria='Attrazione'"
        scope.launch{
        val userAPI = ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazioneLista)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    Log.d("JSON", response.body().toString())
                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset")) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset per la popolazione")
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        Log.d("RISULTATO DELLA QUERY PER LA POPOLAZIONE", querySetArray.toString())
                        if (querySetArray != null && querySetArray.size() > 0) {
                            Log.e("Ciao", "sto per entrare nel for")
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
                                        Log.d("RESPONSE", response.isSuccessful.toString())
                                        if (response.isSuccessful) {
                                            Log.e(
                                                "Ciao",
                                                "sono dentro il blocco della foto DENTRO IS SUCCESSFULL"
                                            )
                                            val responseBody = response.body()
                                            if (responseBody != null) {
                                                val inputStream = responseBody.byteStream()
                                                bitmap = BitmapFactory.decodeStream(inputStream)
                                                homeItem.add(
                                                    HomeItemModel(
                                                        elemento.get("idPosti").asInt,
                                                        bitmap,
                                                        elemento.get("nome").toString(),
                                                        elemento.get("numRecensioni").toString(),
                                                        elemento.get("valutazione").toString()
                                                    )
                                                )
                                                // Aggiorna l'adapter dopo aver aggiunto l'elemento
                                                homeAdapter.notifyDataSetChanged()
                                            }
                                            homeAdapter.setOnItemClickListener { homeItemModel ->
                                                val id = homeItemModel.id
                                                val childFragment = AnteprimaPostoFragment()
                                                requireActivity().intent.putExtra("idPosto", id.toString())
                                                requireActivity().intent.putExtra("idUtente", idUtente.toString())
                                                requireActivity().intent.putExtra("provenienza", "attrazioniFragment")
                                                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
                                                fragmentTransaction.addToBackStack(null)
                                                fragmentTransaction.commit()
                                            }

                                            homeAdapter.setOnToggleClickListener { homeItemModel, isChecked ->
                                                if (isChecked) {
                                                    // Il toggle è stato selezionato
                                                    val idPosto = homeItemModel.id
                                                    Log.e("ho checkato", idPosto.toString())
                                                    setPreferiti(idPosto, idUtente)
                                                    // ... altre azioni da eseguire
                                                } else {
                                                    // Il toggle è stato deselezionato
                                                    val idPosto = homeItemModel.id
                                                    Log.e("ho decheckato", idPosto.toString())
                                                    rimuoviPreferiti(idPosto, idUtente)
                                                }
                                            }

                                        }
                                        Log.d(
                                            "DIMENSIONE DELLA HOME ITEM",
                                            homeItem.size.toString()
                                        )
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        Toast.makeText(
                                            requireContext(),
                                            "L'immagine non è stata trovata correttamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })

                            }


                            //configuriamo l'adapter con la recycler view
                            binding.RecyclerViewVerticaleA.adapter = homeAdapter

                        }
                    }


                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
                Log.e("ciao", t.toString() + " " + t.message.toString())
            }
        })
    }
    }



    private fun setRecyclerViewOrizzontale(idUtente: Int) {
        val horizontalItem = ArrayList<HorizontalRecyclerItem>()
        val horizontalAdapter = HorizontalItemAdapter(horizontalItem)
        val scope = CoroutineScope(Dispatchers.Default)
        //imposto adapter sulla recycler view
        binding.RecyclerViewOrizzontaleA.adapter = horizontalAdapter

        val queryPopolazioneTop5 = "SELECT idPosti,nome,valutazione,numRecensioni,foto,descrizione FROM Posti WHERE categoria='Attrazione' ORDER BY valutazione DESC  LIMIT 5"
        scope.launch {
            val userAPI = ClientNetwork.retrofit
            val call = userAPI.cerca(queryPopolazioneTop5)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        Log.d("JSON", response.body().toString())
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset")) {
                            Log.e(
                                "Ciao",
                                "HO OTTENUTO IL JSONOBJECT come queryset per la popolazione della recycler orizzontale"
                            )
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            Log.d(
                                "RISULTATO DELLA QUERY PER LA POPOLAZIONE ORIZZONTALE",
                                querySetArray.toString()
                            )
                            if (querySetArray != null && querySetArray.size() > 0) {
                                Log.e("Ciao", "sto per entrare nel for DELLA RECYCLER ORIZZONTALE")
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
                                            Log.d("RESPONSE", response.isSuccessful.toString())
                                            if (response.isSuccessful) {
                                                Log.e(
                                                    "Ciao",
                                                    "sono dentro il blocco della foto DENTRO IS SUCCESSFULL"
                                                )
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
                                                        "attrazioniFragment"
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
                                            Log.d(
                                                "DIMENSIONE DELLA HOME ITEM",
                                                horizontalItem.size.toString()
                                            )
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
                                binding.RecyclerViewOrizzontaleA.adapter = horizontalAdapter

                            }
                        }


                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    Log.e("ciao", t.toString() + " " + t.message.toString())
                }
            })
        }
    }
    private fun rimuoviPreferiti(idPosto: Int, idUtente: Int) {
        Log.e("sono etrato nel remove","1")
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
                    Log.e("sono","nel secondo on failure")
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
                                Log.e("ERRORE","POSTO GIA NEI PREFERITI")
                                Log.d("res:",querySetArray.toString())
                                Toast.makeText(requireContext(), "Hai già inserito questo luogo tra i preferiti!", Toast.LENGTH_SHORT).show()
                            } else {
                                //il posto non c'è posso fare query
                                val queryInserisciFav= "INSERT INTO Preferiti (ref_utente, ref_posto) VALUES ('${idUtente}','${idPosto}')"
                                val call = userAPI.inserisci(queryInserisciFav)
                                call.enqueue(object : Callback<JsonObject> {
                                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                        if (response.isSuccessful) {
                                            // L'inserimento dell'utente online è avvenuto con successo e lo inserisco in locale
                                            Log.d("NEL D HO INSERITO", idUtente.toString() + idPosto.toString())
                                            Toast.makeText(requireContext(), "Inserimento nei preferiti avvenuto!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                        Log.e("ciao",t.toString() + " " + t.message.toString())
                                    }
                                })

                            }

                        }
                    }


                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    Log.e("sono","nel secondo on failure")
                }
            })
        }
    }
}