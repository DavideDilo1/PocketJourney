package com.example.pocketjourney.home.sezioniHome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.HomeAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentPacchettiBinding
import com.example.pocketjourney.home.AnteprimaPostoFragment
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PacchettiFragment : Fragment() {

    private lateinit var binding: FragmentPacchettiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPacchettiBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        binding.recyclerPacchetti.layoutManager = LinearLayoutManager(requireContext())
        Log.e("ATTENZIONEEEEE","HA APERTO Li pack" + idUtente)

        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
        }

        binding.backArrowP.setOnClickListener {
            val childFragment = HomeFragmentNew()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            requireActivity().intent.putExtra("idUtente",idUtente)
            fragmentTransaction.replace(R.id.framePacchetti, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        return binding.root
    }

    private fun setRecyclerView(idUtente: Int) {
        val homeItem = ArrayList<HomeItemModel>()
        val homeAdapter = HomeAdapter(homeItem)
        //imposto adapter sulla recycler view
        binding.recyclerPacchetti.adapter=homeAdapter

        val queryPopolazionePacchetti= "SELECT idPacchetto,nome,valutazione,numRecensioni,foto FROM Pacchetti"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazionePacchetti)
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
                                                        elemento.get("idPacchetto").asInt,
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
                                                val childFragment = PaginaPacchettoFragment()
                                                requireActivity().intent.putExtra("idPacchetto",id.toString())
                                                requireActivity().intent.putExtra("idUtente",idUtente.toString())
                                                Log.d("CIAO SONO IL CLICK LISTENER STO PASSANDO", id.toString() + idUtente.toString())
                                                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                fragmentTransaction.replace(R.id.framePacchetti, childFragment)
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
                            binding.recyclerPacchetti.adapter = homeAdapter

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