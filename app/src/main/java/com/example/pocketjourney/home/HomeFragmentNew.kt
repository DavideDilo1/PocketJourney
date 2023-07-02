package com.example.pocketjourney.home

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import com.example.pocketjourney.databinding.FragmentHomeNewBinding
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketjourney.*
import com.example.pocketjourney.adapter.HomeAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.home.sezioniHome.*
import com.example.pocketjourney.model.HomeItemModel
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
import androidx.appcompat.widget.SearchView


class HomeFragmentNew : Fragment() {
    private lateinit var binding: FragmentHomeNewBinding
    
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private lateinit var textView3: TextView

    private lateinit var homeRecycle: RecyclerView

    private lateinit var searchView: SearchView
    private lateinit var ristorantiButton: Button
    private lateinit var hotelButton: Button
    private lateinit var attrazioniButton: Button
    private lateinit var home_background: ImageView
    private lateinit var ideaButton: ImageButton
    private var homeItem = ArrayList<HomeItemModel>()
    private var homeAdapter = HomeAdapter(homeItem)

    private var isHomeOpen:Boolean=true


    @TargetApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeNewBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val email = requireActivity().intent.getStringExtra("email")
        //definiamo la recycler view della home:

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.e("ATTENZIONEEEEE","HA APERTO LA HOME" + idUtente + " " + email)
        Log.e("OPEN:",isHomeOpen.toString())
        requireActivity().intent.putExtra("isHomeOpen",isHomeOpen.toString())

        requireActivity().intent.putExtra("isHomeOpen",isHomeOpen.toString())
        if (idUtente != null) {
                setRecyclerView(idUtente.toInt())
                val count =requireActivity().supportFragmentManager.backStackEntryCount.toString()
            Log.e("IMPORTANTISSIMISSIMO",count)
        } else {
            binding.normalConstraintHome.visibility=View.GONE
            binding.errorConstraint.visibility=View.VISIBLE
        }

        //TODO: aggiungere le animazioni alla recycler view

        home_background = binding.imageBackground
        ristorantiButton = binding.resturantButton
        attrazioniButton = binding.attractionButton
        hotelButton = binding.hotelButton

        textView = binding.testoCosaStaiCercando
        textView2 = binding.testoHome
        textView3 = binding.testoMiglioriMete

        ideaButton = binding.ideaButton

        homeRecycle = binding.homeRecyclerView
        searchView = binding.searchViewHome



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


        ristorantiButton.setOnClickListener{
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameNewHomeLayout, RistorantiFragment())
                .addToBackStack(null)
                .commit()
            isHomeOpen=false
            requireActivity().intent.putExtra("isHomeOpen",isHomeOpen.toString())
            Log.e("OPEN:",isHomeOpen.toString())
        }

        hotelButton.setOnClickListener{
          /*  val childFragment = HotelFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()*/
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameNewHomeLayout, HotelFragment())
                .addToBackStack(null)
                .commit()
        }

        attrazioniButton.setOnClickListener{
           /* val childFragment = AttrazioniFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()*/
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameNewHomeLayout, AttrazioniFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.ideaButton.setOnClickListener{
          /*  val childFragment = ConsigliatiFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()*/
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameNewHomeLayout, ConsigliatiFragment())
                .commit()

        }

        binding.pacchettiButton.setOnClickListener {
            //val childFragment = PacchettiFragment()
           // val fragmentTransaction = childFragmentManager.beginTransaction()
            //requireActivity().intent.putExtra("idUtente",idUtente)
            //fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            //fragmentTransaction.addToBackStack(null)
            //fragmentTransaction.commit()
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.frameNewHomeLayout, PacchettiFragment())
                .commit()
        }
        return binding.root
    }



    private fun setRecyclerView(idUtente: Int) {
       // val homeItem = ArrayList<HomeItemModel>()
       // val homeAdapter = HomeAdapter(homeItem)
        //imposto adapter sulla recycler view
        binding.homeRecyclerView.adapter=homeAdapter
        val scope = CoroutineScope(Dispatchers.Default)
        val queryPopolazioneLista= "SELECT idPosti,nome,valutazione,numRecensioni,foto FROM Posti ORDER BY valutazione DESC LIMIT 10"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazioneLista)
        scope.launch{
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {

                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                for(i in querySetArray){
                                    var bitmap : Bitmap
                                    val elemento= i as JsonObject
                                    val foto=elemento.get("foto").asString
                                    val downloadFotoPosto=userAPI.getAvatar(foto)
                                    downloadFotoPosto.enqueue(object : Callback<ResponseBody> {
                                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
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
                                                            elemento.get("numRecensioni").toString(),
                                                            elemento.get("valutazione").toString()
                                                        )
                                                    )
                                                    // Aggiorna l'adapter dopo aver aggiunto l'elemento
                                                    homeAdapter.notifyDataSetChanged()
                                                }
                                                homeAdapter.setOnItemClickListener { homeItemModel ->
                                                    val id = homeItemModel.id
                                                    requireActivity().intent.putExtra("idPosto",id.toString())
                                                    requireActivity().intent.putExtra("idUtente",idUtente.toString())
                                                    val manager=requireActivity().supportFragmentManager
                                                    requireActivity().intent.putExtra("idUtente",idUtente)
                                                    manager.beginTransaction().replace(R.id.frameNewHomeLayout, AnteprimaPostoFragment())
                                                        .addToBackStack(null)
                                                        .commit()
                                                }

                                                homeAdapter.setOnToggleClickListener { homeItemModel, isChecked ->
                                                    if (isChecked) {
                                                        // Il toggle è stato selezionato
                                                        val idPosto = homeItemModel.id
                                                        Log.e("ho checkato",idPosto.toString())
                                                        setPreferiti(idPosto,idUtente)
                                                        // ... altre azioni da eseguire
                                                    } else {
                                                        // Il toggle è stato deselezionato
                                                        val idPosto = homeItemModel.id
                                                        Log.e("ho decheckato", idPosto.toString())
                                                        rimuoviPreferiti(idPosto,idUtente)
                                                    }
                                                }


                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                            Log.e("sono","nel primo on failure")
                                            binding.normalConstraintHome.visibility=View.GONE
                                            binding.errorConstraint.visibility=View.VISIBLE
                                            Toast.makeText(requireContext(), "L'immagine non è stata trovata correttamente", Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                }


                                //configuriamo l'adapter con la recycler view
                                binding.homeRecyclerView.adapter = homeAdapter

                            }
                        }


                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    Log.e("sono","nel secondo on failure")
                    binding.normalConstraintHome.visibility=View.GONE
                    binding.errorConstraint.visibility=View.VISIBLE
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

    override fun onResume() {
        super.onResume()
        isHomeOpen = true
        requireActivity().intent.putExtra("isHomeOpen", isHomeOpen.toString())
        Log.e("OPEN DOPO BACK:", isHomeOpen.toString())
    }
}



