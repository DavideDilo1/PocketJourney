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
import com.example.pocketjourney.home.HomeFragmentNew
import com.example.pocketjourney.model.HomeItemModel
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        requireActivity().intent.putExtra("frame","framePacchetti")
        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
        }

        binding.backArrowP.setOnClickListener {
            val manager=requireActivity().supportFragmentManager
            requireActivity().intent.putExtra("idUtente",idUtente)
            manager.beginTransaction().replace(R.id.framePacchetti, HomeFragmentNew())
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun setRecyclerView(idUtente: Int) {
        val scope = CoroutineScope(Dispatchers.Default)
        val homeItem = ArrayList<HomeItemModel>()
        val homeAdapter = HomeAdapter(homeItem)
        homeAdapter.showFavoriteButton = false
        homeAdapter.notifyDataSetChanged()
        //imposto adapter sulla recycler view
        binding.recyclerPacchetti.adapter=homeAdapter

        val queryPopolazionePacchetti= "SELECT idPacchetto,nome,valutazione,numRecensioni,foto FROM Pacchetti"
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazionePacchetti)

        scope.launch {
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
                                                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                    fragmentTransaction.replace(R.id.framePacchetti, childFragment)
                                                    fragmentTransaction.addToBackStack(null)
                                                    fragmentTransaction.commit()
                                                }

                                            }
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
                }
            })
        }
    }

}