package com.example.pocketjourney.preferiti

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.FavAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentListaPreferitiBinding
import com.example.pocketjourney.home.AnteprimaPostoFragment
import com.example.pocketjourney.model.HomeItemModel
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        requireActivity().intent.putExtra("frame","frameListaPreferiti")
        if (idUtente != null) {
            setRecyclerView(idUtente.toInt())
        }else{
            binding.preferitiText2.visibility = View.GONE
            binding.textNoFavorite.visibility = View.VISIBLE
            binding.textNoFavorite2.visibility = View.VISIBLE
            binding.imageNoPreferiti.visibility = View.VISIBLE
            binding.preferitiLinearL.visibility = View.GONE
            binding.scrollPreferiti.visibility = View.GONE
        }

        // Inflate the layout for this fragment
        return binding.root
    }




    private fun setRecyclerView(idUtente: Int) {
        val scope = CoroutineScope(Dispatchers.Default)
        val favItem = ArrayList<HomeItemModel>()
        val favAdapter = FavAdapter(favItem)
        //imposto adapter sulla recycler view
        binding.recyclerMieiPreferiti.adapter = favAdapter
        scope.launch {
            val queryPopolazioneLista =
                "SELECT P.idPosti, P.nome, P.valutazione, P.numRecensioni, P.foto FROM Preferiti AS PR JOIN Posti AS P ON PR.ref_posto = P.idPosti WHERE PR.ref_utente = '${idUtente}'"
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
                                                    favItem.add(
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
                                                    favAdapter.notifyDataSetChanged()
                                                }
                                                favAdapter.setOnItemClickListener { homeItemModel ->
                                                    val id = homeItemModel.id
                                                    val childFragment = AnteprimaPostoFragment()
                                                    requireActivity().intent.putExtra("idPosto", id.toString())
                                                    requireActivity().intent.putExtra("idUtente", idUtente.toString())
                                                    requireActivity().intent.putExtra("provenienza", "preferiti")
                                                    val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                                                    fragmentTransaction.replace(R.id.frameListaPreferiti, childFragment)
                                                    fragmentTransaction.addToBackStack(null)
                                                    fragmentTransaction.commit()
                                                }

                                                favAdapter.setOnToggleClickListener { homeItemModel, isChecked ->
                                                    if (isChecked) {
                                                        // Il toggle è stato selezionato
                                                        val idPosto = homeItemModel.id
                                                        rimuoviPreferiti(idPosto,idUtente)
                                                        // ... altre azioni da eseguire
                                                        favItem.removeAll { it.id == idPosto }
                                                        favAdapter.notifyDataSetChanged()
                                                    }
                                                }

                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                            Toast.makeText(requireContext(), "L'immagine non è stata trovata correttamente", Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                }


                                //configuriamo l'adapter con la recycler view
                                binding.recyclerMieiPreferiti.adapter = favAdapter

                            }else{
                                binding.preferitiText2.visibility = View.GONE
                                binding.textNoFavorite.visibility = View.VISIBLE
                                binding.textNoFavorite2.visibility = View.VISIBLE
                                binding.imageNoPreferiti.visibility = View.VISIBLE
                                binding.preferitiLinearL.visibility = View.GONE
                                binding.scrollPreferiti.visibility = View.GONE
                            }
                        }


                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    binding.preferitiText2.visibility = View.GONE
                    binding.textNoFavorite.visibility = View.VISIBLE
                    binding.textNoFavorite2.visibility = View.VISIBLE
                    binding.imageNoPreferiti.visibility = View.VISIBLE
                    binding.preferitiLinearL.visibility = View.GONE
                    binding.scrollPreferiti.visibility = View.GONE
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
}
