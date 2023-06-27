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
//import com.example.pocketjourney.adapter.MainRecyclerAdapter
import com.example.pocketjourney.adapter.HorizontalItemAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentRistorantiBinding
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.example.pocketjourney.model.HorizontalRecyclerItem
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RistorantiFragment : Fragment() {

    private lateinit var binding: FragmentRistorantiBinding

    private lateinit var topImage: ImageView

    private lateinit var recyclerOrizzontale: RecyclerView
    private lateinit var recyclerVerticale: RecyclerView

    private lateinit var titoloFinestra: TextView
    private lateinit var testoTutti: TextView
    private lateinit var testoCategoria: TextView
    private lateinit var searchView: SearchView
    private lateinit var back_arrowR: ImageView

    private lateinit var anim_from_bottom: Animation
    private lateinit var anim_from_top: Animation
    private lateinit var anim_from_left: Animation
    private lateinit var anim_from_right: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRistorantiBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        Log.e("ATTENZIONEEEEE","HA APERTO LA ristoranti fragment " + idUtente)

        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
            setRecyclerViewOrizzontale(idUtente.toInt())
        }

        binding.RecyclerViewOrizzontaleR.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.RecyclerViewVerticaleR.layoutManager = LinearLayoutManager(requireContext())

        //prima categoria






/*
        val allRestaurant: MutableList<AllRestaurant> = ArrayList()
        allRestaurant.add(AllRestaurant("I migliori ristoranti di sushi", restaurantItemList1))
        allRestaurant.add(AllRestaurant("Le migliori pizzerie", restaurantItemList2))
*/
        //  setHorizontalRecycler(allRestaurant)


        //  val ristorantiOrizzAdapter2 = RestaurantItemAdapter(requireContext(), restaurantItemList2)

/*
        ristorantiOrizzAdapter2.onItemClick = {

            val bundle = Bundle()
            bundle.putParcelable("ristoranti", it)

            val childFragment = AnteprimaPostoFragment()
            childFragment.arguments=bundle

            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.frameRistoranti, childFragment)
                .addToBackStack(null)
                .commit()

        }*/

        //QUESTA è LA RECYCLER VERTICALE CON TUTTI I RISTORANTI:

        binding.backArrowR.setOnClickListener() {
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameRistoranti, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        topImage = binding.imageBackground
        recyclerOrizzontale = binding.RecyclerViewOrizzontaleR

        recyclerVerticale = binding.RecyclerViewVerticaleR

        titoloFinestra = binding.testoRistoranti

        testoTutti = binding.testoTuttiR
        testoCategoria = binding.testoCategoriaR
        searchView = binding.searchView


        anim_from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)
        anim_from_top = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_top)
        anim_from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        anim_from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)


        topImage.animation = anim_from_top
        recyclerOrizzontale.animation = anim_from_left
        recyclerVerticale.animation = anim_from_bottom
        testoTutti.animation = anim_from_left
        testoCategoria.animation = anim_from_left
        titoloFinestra.animation = anim_from_top
        searchView.animation = anim_from_right


        return binding.root
    }

    private fun setRecyclerView(idUtente: Int) {
        val homeItem = ArrayList<HomeItemModel>()
        val homeAdapter = HomeAdapter(homeItem)
        //imposto adapter sulla recycler view
        binding.RecyclerViewVerticaleR.adapter=homeAdapter

        val queryPopolazioneLista= "SELECT idPosti,nome,valutazione,numRecensioni,foto FROM Posti WHERE categoria='Ristorante'"
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
                                                requireActivity().intent.putExtra("provenienza","ristorantiFragment")
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
                            binding.RecyclerViewVerticaleR.adapter = homeAdapter

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



    private fun setRecyclerViewOrizzontale(idUtente: Int) {
        val horizontalItem = ArrayList<HorizontalRecyclerItem>()
        val horizontalAdapter = HorizontalItemAdapter(horizontalItem)
        //imposto adapter sulla recycler view
        binding.RecyclerViewOrizzontaleR.adapter=horizontalAdapter

        val queryPopolazioneTop5= "SELECT idPosti,nome,valutazione,numRecensioni,foto,descrizione FROM Posti WHERE categoria='Ristorante' ORDER BY valutazione DESC  LIMIT 5"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazioneTop5)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body()
                    Log.d("JSON", response.body().toString())
                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset per la popolazione della recycler orizzontale" )
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        Log.d("RISULTATO DELLA QUERY PER LA POPOLAZIONE ORIZZONTALE", querySetArray.toString())
                        if (querySetArray != null && querySetArray.size()>0){
                            Log.e("Ciao", "sto per entrare nel for DELLA RECYCLER ORIZZONTALE")
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
                                                horizontalItem.add(
                                                    HorizontalRecyclerItem(
                                                        elemento.get("idPosti").asInt,
                                                        bitmap,
                                                        elemento.get("nome").toString(),
                                                        elemento.get("numRecensioni").toString(),
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
                                                requireActivity().intent.putExtra("idPosto",id.toString())
                                                requireActivity().intent.putExtra("idUtente",idUtente.toString())
                                                requireActivity().intent.putExtra("provenienza","ristorantiFragment")
                                                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                fragmentTransaction.replace(R.id.frameNewHomeLayout, childFragment)
                                                fragmentTransaction.addToBackStack(null)
                                                fragmentTransaction.commit()
                                            }

                                        }
                                        Log.d("DIMENSIONE DELLA HOME ITEM", horizontalItem.size.toString())
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        Toast.makeText(requireContext(), "L'immagine non è stata trovata correttamente", Toast.LENGTH_SHORT).show()
                                    }
                                })

                            }


                            //configuriamo l'adapter con la recycler view
                            binding.RecyclerViewOrizzontaleR.adapter = horizontalAdapter

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