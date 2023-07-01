package com.example.pocketjourney.preferiti

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
import com.example.pocketjourney.databinding.FragmentAttrazioniBinding
import com.example.pocketjourney.databinding.FragmentListaPreferitiBinding
import com.example.pocketjourney.home.AnteprimaPostoFragment
import com.example.pocketjourney.model.HomeItemModel
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaPreferitiFragment : Fragment() {
    private lateinit var binding:FragmentListaPreferitiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaPreferitiBinding.inflate(inflater)
        binding.recyclerMieiPreferiti.layoutManager = LinearLayoutManager(requireContext())
        val idUtente = requireActivity().intent.getStringExtra("idUtente")

        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
        }

        // Inflate the layout for this fragment
        return binding.root
    }






    private fun setRecyclerView(idUtente: Int) {
        val homeItem = ArrayList<HomeItemModel>()
        val homeAdapter = HomeAdapter(homeItem)
        //imposto adapter sulla recycler view
        binding.recyclerMieiPreferiti.adapter=homeAdapter

        val queryPopolazioneLista= "SELECT P.idPosti, P.nome, P.valutazione, P.numRecensioni, P.foto FROM Preferiti AS PR JOIN Posti AS P ON PR.ref_posto = P.idPosti WHERE PR.ref_utente = '${idUtente}'"
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
                                                requireActivity().intent.putExtra("provenienza","preferiti")
                                                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                fragmentTransaction.replace(R.id.frameListaPreferiti, childFragment)
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
                            binding.recyclerMieiPreferiti.adapter = homeAdapter

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
