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

    private lateinit var anim_from_bottom: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation

    @TargetApi(Build.VERSION_CODES.S)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeNewBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        //definiamo la recycler view della home:

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.e("ATTENZIONEEEEE","HA APERTO LA HOME" + idUtente)
        if (idUtente != null) {
                setRecyclerView(idUtente.toInt())

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
        searchView = binding.searchViewHome

        ideaButton = binding.ideaButton

        homeRecycle = binding.homeRecyclerView

        //load animations
        anim_from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        anim_from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)
        //set animations

        //cardView.setAnimation(anim_from_button)
     //   cardView2.setAnimation(anim_from_button)
      //  cardView3.setAnimation(anim_from_button)
        /*
        textView.animation = anim_from_top
        textView2.animation = anim_from_top
        textView3.animation = anim_from_bottom
        ideaButton.animation = anim_from_right

        searchView.animation = anim_from_left

        home_background.animation = anim_from_top
        ristorantiButton.animation = anim_from_left
        attrazioniButton.animation = anim_from_left
        hotelButton.animation = anim_from_left


        homeRecycle.setAnimation(anim_from_bottom)
*/

       // favoriteButton1 = binding.FavoriteButton
      //  favoriteButton2 = binding.FavoriteButton2
      //  favoriteButton3 = binding.FavoriteButton3

/*
        favoriteButton1.setOnClickListener {

            if(favoriteButton1.isChecked()){

                Toast.makeText(requireContext(), "Aggiunto ai Preferiti", Toast.LENGTH_SHORT).show()
            }else
            }
        }*/


        ristorantiButton.setOnClickListener(View.OnClickListener { view ->
            val childFragment = RistorantiFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        })

        hotelButton.setOnClickListener{
            val childFragment = HotelFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        attrazioniButton.setOnClickListener{
            val childFragment = AttrazioniFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.ideaButton.setOnClickListener{
            val childFragment = ConsigliatiFragment()
            requireActivity().intent.putExtra("idUtente",idUtente)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

        binding.pacchettiButton.setOnClickListener {
            val childFragment = PacchettiFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return binding.root
    }

    private fun setRecyclerView(idUtente: Int) {
        val homeItem = ArrayList<HomeItemModel>()
        val homeAdapter = HomeAdapter(homeItem)
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
                            Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset per la popolazione" )
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                Log.e("Ciao", "sto per entrare nel for")
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
                                                    val childFragment = AnteprimaPostoFragment()
                                                    requireActivity().intent.putExtra("idPosto",id.toString())
                                                    requireActivity().intent.putExtra("idUtente",idUtente.toString())
                                                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                    fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
                                                    fragmentTransaction.addToBackStack(null)
                                                    fragmentTransaction.commit()
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





}



