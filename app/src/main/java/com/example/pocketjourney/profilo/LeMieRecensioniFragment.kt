package com.example.pocketjourney.profilo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketjourney.ProfileFragment
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.PrenotazioniAdapter
import com.example.pocketjourney.adapter.RecensioniAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DbHelper
import com.example.pocketjourney.databinding.FragmentLeMieRecensioniBinding
import com.example.pocketjourney.model.PrenotazioniModel
import com.example.pocketjourney.model.RecensioniModel
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeMieRecensioniFragment : Fragment() {
    private lateinit var binding: FragmentLeMieRecensioniBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLeMieRecensioniBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")

        binding.recyclerMieRecensioni.layoutManager = LinearLayoutManager(requireContext())

        if (idUtente != null) {
            setRecyclerView(idUtente)
        } else {
            binding.recyclerMieRecensioni.visibility=View.GONE
            binding.nessunaRecImg.visibility=View.VISIBLE
            binding.textNessunDato.visibility=View.VISIBLE
            binding.textNessunDato2.visibility=View.VISIBLE
        }

        binding.backArrowMyRec.setOnClickListener {
            val childFragment = ProfileFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLeMieRecensioni, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return binding.root
    }


    private fun setRecyclerView(idUtente: String) {
        val reviewItem = ArrayList<RecensioniModel>()
        val reviewAdapter = RecensioniAdapter(reviewItem)
        //imposto adapter sulla recycler view
        binding.recyclerMieRecensioni.adapter=reviewAdapter
        val scope = CoroutineScope(Dispatchers.Default)
        val userAPI= ClientNetwork.retrofit
        val queryPopolazioneRec= "SELECT rp.idRecensione, p.nome AS nome, 'Posto' AS tipo, rp.titolo, rp.testo, rp.valutazione, rp.data\n" +
                "FROM RecensioniPosti rp\n" +
                "JOIN Posti p ON rp.ref_posto = p.idPosti\n" +
                "WHERE rp.ref_utente = '${idUtente}'\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT rpa.idRecensione, pa.nome AS nome, 'Pacchetto' AS tipo, rpa.titolo, rpa.testo, rpa.valutazione, rpa.data\n" +
                "FROM RecensioniPacchetti rpa\n" +
                "JOIN Pacchetti pa ON rpa.ref_pacchetto = pa.idPacchetto\n" +
                "WHERE rpa.ref_utente = '${idUtente}';"

        val call = userAPI.cerca(queryPopolazioneRec)
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
                                    val elemento= i as JsonObject
                                    Log.e("Ciao", "sono dentro il blocco della foto DENTRO IS SUCCESSFULL")
                                    reviewItem.add(
                                        RecensioniModel(
                                            elemento.get("nome").asString,
                                            elemento.get("titolo").toString(),
                                            elemento.get("testo").toString(),
                                            elemento.get("valutazione").asFloat,
                                            elemento.get("data").asString
                                        )
                                    )
                                    // Aggiorna l'adapter dopo aver aggiunto l'elemento
                                    reviewAdapter.notifyDataSetChanged()
                                }

                                //configuriamo l'adapter con la recycler view
                                binding.recyclerMieRecensioni.adapter = reviewAdapter

                            } else {
                                binding.nessunaRecImg.visibility=View.VISIBLE
                                binding.textNessunDato.visibility=View.VISIBLE
                                binding.textNessunDato2.visibility=View.VISIBLE
                            }
                        }


                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    binding.nessunaRecImg.visibility = View.VISIBLE
                }
            })
        }
    }

}