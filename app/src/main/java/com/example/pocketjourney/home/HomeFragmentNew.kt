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
    private lateinit var favoriteButton1: ToggleButton
    private lateinit var favoriteButton2: ToggleButton
    private lateinit var favoriteButton3: ToggleButton
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


        //un ITEM VIEW MODEL é FATTO: val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float


        //passiamo l'array list all'adapter:

       // val homeAdapter = HomeAdapter(homeItem)

        //configuriamo l'adapter con la recycler view
       // binding.homeRecyclerView.adapter = homeAdapter
        Log.e("ATTENZIONEEEEE","HA APERTO LA HOME" + idUtente)

        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
        }
   /*     homeAdapter.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("home", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameNewHomeLayout, childFragment)
                .addToBackStack(null)
                .commit()

        } */

       //onclick listener su un elemento della recycler.







        // cardView = binding.cardView
        // cardView2 = binding.cardView2
        // cardView3 = binding.cardView3


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


       // favoriteButton1 = binding.FavoriteButton
      //  favoriteButton2 = binding.FavoriteButton2
      //  favoriteButton3 = binding.FavoriteButton3

/*
        favoriteButton1.setOnClickListener {

            if(favoriteButton1.isChecked()){
                //TODO: se cliccato aggiungi ai preferiti
                Toast.makeText(requireContext(), "Aggiunto ai Preferiti", Toast.LENGTH_SHORT).show()
            }else{
                //TODO: se non cliccato rimuovi dai preferiti

            }
        }*/

        ideaButton.setOnClickListener{
            //TODO: implementare le schermate idea
        }

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

        val queryPopolazioneLista= "SELECT idPosti,nome,valutazione,numRecensioni,foto FROM Posti ORDER BY valutazione DESC"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazioneLista)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    Log.d("JSON", response.body().toString())
                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset per la popolazione" )
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        Log.d("RISULTATO DELLA QUERY PER LA POPOLAZIONE", querySetArray.toString())
                        if (querySetArray != null && querySetArray.size()>0){
                            Log.e("Ciao", "sto per entrare nel for")
                            for(i in querySetArray){
                                var bitmap : Bitmap
                                val elemento= i as JsonObject
                                val foto=elemento.get("foto").asString
                                val downloadFotoPosto=userAPI.getAvatar(foto)
                                downloadFotoPosto.enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                        Log.d("RESPONSE", response.isSuccessful.toString())
                                        if (response.isSuccessful) {
                                            Log.e("Ciao", "sono dentro il blocco della foto DENTRO IS SUCCESSFULL")
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

                                        }
                                        Log.d("DIMENSIONE DELLA HOME ITEM", homeItem.size.toString())
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
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
                Log.e("ciao", t.toString() + " " + t.message.toString())
            }
        })
    }





}



