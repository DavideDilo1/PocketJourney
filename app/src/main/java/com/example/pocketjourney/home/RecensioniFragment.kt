package com.example.pocketjourney.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketjourney.R
import com.example.pocketjourney.adapter.RecensioniAdapter
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentRecensioniBinding
import com.example.pocketjourney.home.sezioniHome.PaginaPacchettoFragment
import com.example.pocketjourney.model.RecensioniModel
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecensioniFragment : Fragment() {

    private lateinit var binding: FragmentRecensioniBinding
    private lateinit var querySetDatiRecensione:String
    private lateinit var queryInserisciRec:String
    private var selectedRating:Float=0f
    private lateinit var provenienza:String
    private lateinit var idPosto:String
    private lateinit var queryPopolazioneListaRec:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecensioniBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        provenienza= requireActivity().intent.getStringExtra("provenienzaRec").toString()
        requireActivity().intent.putExtra("frame","frameRecensioni")

        if(provenienza=="pacchetto"){
            idPosto = requireActivity().intent.getStringExtra("idPack").toString()
            querySetDatiRecensione = "SELECT nome,numRecensioni,valutazione FROM Pacchetti WHERE idPacchetto = '$idPosto'"
            queryInserisciRec="INSERT INTO RecensioniPacchetti (ref_utente, ref_pacchetto, titolo, testo, valutazione, data)"
            queryPopolazioneListaRec= "SELECT Utente.Nome, RecensioniPacchetti.titolo, RecensioniPacchetti.testo, RecensioniPacchetti.valutazione, RecensioniPacchetti.data FROM RecensioniPacchetti JOIN Utente ON Utente.idUtente = RecensioniPacchetti.ref_utente WHERE RecensioniPacchetti.ref_pacchetto ='$idPosto'"
        } else {
            idPosto = requireActivity().intent.getStringExtra("idPosto").toString()
            querySetDatiRecensione = "SELECT nome,numRecensioni,valutazione FROM Posti WHERE idPosti = '$idPosto'"
            queryInserisciRec="INSERT INTO RecensioniPosti (ref_utente, ref_posto, titolo, testo, valutazione, data)"
            queryPopolazioneListaRec= "SELECT Utente.Nome, RecensioniPosti.titolo, RecensioniPosti.testo, RecensioniPosti.valutazione, RecensioniPosti.data FROM RecensioniPosti JOIN Utente ON Utente.idUtente = RecensioniPosti.ref_utente WHERE RecensioniPosti.ref_posto ='$idPosto'"
        }



        binding.recyclerReview.layoutManager = LinearLayoutManager(requireContext())
        Log.e("dati:", idUtente +" "+ idPosto +" " + querySetDatiRecensione + " " + queryInserisciRec + " " + provenienza)
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(querySetDatiRecensione)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //posso effettuare il login online
                    Log.e("Ciao","posso cambiare i dati con dbonline")
                    val jsonObject = response.body() // Ottieni il JSON come JsonObject
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset" )
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size()>0){
                            val primoPosto=querySetArray[0].asJsonObject //prendo la prima corrispondenza
                            if (primoPosto!=null && primoPosto.has("numRecensioni")){
                                //prelevo i campi e li setto nel fragment
                                //in base alla valutazione avrò diverse progressbar
                                val recTotali = primoPosto.get("numRecensioni").asInt
                                val valutazione=primoPosto.get("valutazione").asFloat
                                binding.titoloPostoRec.text=primoPosto.get("nome").asString
                                binding.mediaRating.text=valutazione.toString()
                                binding.numeroRecensioni.text=recTotali.toString()
                                binding.ratingBarRec.rating=valutazione
                                if (valutazione<=5.0f && valutazione>=4.6f){
                                    val numRecensioniEccellenti = recTotali*4/10 // Numero di recensioni eccellenti
                                    val numRecensioniMoltoBuone = recTotali*3/10 // Numero di recensioni molto buone
                                    val numRecensioniNellaMedia = recTotali*2/10 // Numero di recensioni nella media
                                    val numRecensioniScarse = recTotali/10 //Numero recessioni basse
                                    val numRecensioniTerribili = recTotali/10 //Numero di recensioni terribili
                                    binding.excellentProgressBar.progress = numRecensioniEccellenti
                                    binding.goodProgressBar.progress = numRecensioniMoltoBuone
                                    binding.averageProgressBar.progress = numRecensioniNellaMedia
                                    binding.poorProgressBar.progress = numRecensioniScarse
                                    binding.terribleProgressBar.progress = numRecensioniTerribili
                                } else if (valutazione<=4.5f && valutazione>3.5f){
                                    val numRecensioniEccellenti = recTotali*3/10 // Numero di recensioni eccellenti
                                    val numRecensioniMoltoBuone = recTotali*4/10 // Numero di recensioni molto buone
                                    val numRecensioniNellaMedia = recTotali*2/10 // Numero di recensioni nella media
                                    val numRecensioniScarse = recTotali/10 //Numero recessioni basse
                                    val numRecensioniTerribili = recTotali/10 //Numero di recensioni terribili
                                    binding.excellentProgressBar.progress = numRecensioniEccellenti
                                    binding.goodProgressBar.progress = numRecensioniMoltoBuone
                                    binding.averageProgressBar.progress = numRecensioniNellaMedia
                                    binding.poorProgressBar.progress = numRecensioniScarse
                                    binding.terribleProgressBar.progress = numRecensioniTerribili
                                } else if (valutazione<=3.5f && valutazione>=2.0f) {
                                    val numRecensioniEccellenti = recTotali*1/10 // Numero di recensioni eccellenti
                                    val numRecensioniMoltoBuone = recTotali*2/10 // Numero di recensioni molto buone
                                    val numRecensioniNellaMedia = recTotali*2/10 // Numero di recensioni nella media
                                    val numRecensioniScarse = recTotali*3/10 //Numero recessioni basse
                                    val numRecensioniTerribili = recTotali*2/10 //Numero di recensioni terribili
                                    binding.excellentProgressBar.progress = numRecensioniEccellenti
                                    binding.goodProgressBar.progress = numRecensioniMoltoBuone
                                    binding.averageProgressBar.progress = numRecensioniNellaMedia
                                    binding.poorProgressBar.progress = numRecensioniScarse
                                    binding.terribleProgressBar.progress = numRecensioniTerribili
                                } else {
                                    val numRecensioniEccellenti = recTotali/10 // Numero di recensioni eccellenti
                                    val numRecensioniMoltoBuone = recTotali*2/10 // Numero di recensioni molto buone
                                    val numRecensioniNellaMedia = recTotali*2/10 // Numero di recensioni nella media
                                    val numRecensioniScarse = recTotali*3/10 //Numero recessioni basse
                                    val numRecensioniTerribili = recTotali*4/10 //Numero di recensioni terribili
                                    binding.excellentProgressBar.progress = numRecensioniEccellenti
                                    binding.goodProgressBar.progress = numRecensioniMoltoBuone
                                    binding.averageProgressBar.progress = numRecensioniNellaMedia
                                    binding.poorProgressBar.progress = numRecensioniScarse
                                    binding.terribleProgressBar.progress = numRecensioniTerribili
                                }
                                if (idPosto != null) {
                                    setRecyclerView(idPosto.toInt())
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
                //login in locale
                Log.e("Ciao","posso cambiare i dati usando ONFAILURE")
            }
        })

        Log.e("sono recensioni fragment mi ha aperto",idUtente + idPosto)

        binding.backArrowRec.setOnClickListener {
            if(provenienza=="pacchetto") {
                val childFragment = PaginaPacchettoFragment()
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameRecensioni, childFragment) // R.id.fragment_container rappresenta l'ID del contenitore del frammento nel layout dell'attività
                fragmentTransaction.addToBackStack(null) // Aggiunge il frammento alla pila retrostante per poter tornare indietro se necessario
                fragmentTransaction.commit()
            } else {
                val childFragment = PaginaPostoFragment()
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frameRecensioni, childFragment).remove(this) // R.id.fragment_container rappresenta l'ID del contenitore del frammento nel layout dell'attività
                fragmentTransaction.addToBackStack(null) // Aggiunge il frammento alla pila retrostante per poter tornare indietro se necessario
                fragmentTransaction.commit()
            }
        }

        binding.ratingBarLasciaRecensione.apply {
            setOnRatingBarChangeListener{ _, rating, _ ->
                selectedRating=rating
                Log.e("ciao", selectedRating.toString())
            }
        }

        binding.sendReviewButton.setOnClickListener {
            val titolo = binding.titleReviewEditText.text.toString()
            val testo=binding.reviewEditText.text.toString()
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)

            if (selectedRating!=0f && idUtente!=null && idPosto!=null){
                inserisciRecensione(idUtente,idPosto,titolo,testo,selectedRating,formattedDate,queryInserisciRec)
            }
        }


        return binding.root
    }


    private fun setRecyclerView(idPosto: Int) {
        val reviewItem = ArrayList<RecensioniModel>()
        val reviewAdapter = RecensioniAdapter(reviewItem)
        //imposto adapter sulla recycler view
        binding.recyclerReview.adapter=reviewAdapter
        val scope = CoroutineScope(Dispatchers.Default)
        val userAPI= ClientNetwork.retrofit
        val call = userAPI.cerca(queryPopolazioneListaRec)
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
                                            elemento.get("Nome").asString,
                                            elemento.get("titolo").toString(),
                                            elemento.get("testo").toString(),
                                            elemento.get("valutazione").asFloat,
                                            elemento.get("data").asString
                                        )
                                    )
                                    // Aggiorna l'adapter dopo aver aggiunto l'elemento
                                    reviewAdapter.notifyDataSetChanged()
                                    Log.d("DIMENSIONE DELLA HOME ITEM", reviewItem.size.toString())





                                }


                                //configuriamo l'adapter con la recycler view
                                binding.recyclerReview.adapter = reviewAdapter

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



    private fun inserisciRecensione(
        idUtente: String,
        idPosto: String,
        titolo: String,
        testo: String,
        rating: Float,
        formattedDate: String,
        primaParteQuery:String
    ) {
        //dati utili per l'inserimetno in locale
        Log.d("DATI OFFLINE=",idPosto + " " + idUtente + " " + titolo + " " + rating.toString() + " "+ formattedDate + " " + testo + " "+ rating)
        val userAPI = ClientNetwork.retrofit
        val secondaParteQuery = " VALUES ('$idUtente', '$idPosto', '$titolo','$testo','$rating','$formattedDate')"
        val queryInserisciRecensioneFin= primaParteQuery+secondaParteQuery
        Log.e("query finale:",queryInserisciRecensioneFin)
        val call = userAPI.inserisci(queryInserisciRecensioneFin)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(),"Recensione inserita!",
                        Toast.LENGTH_SHORT).show()
                    binding.titleReviewEditText.text.clear()
                    binding.reviewEditText.text.clear()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
                //Toast.makeText(requireContext(), t.toString() + " " + t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("ciao",t.toString() + " " + t.message.toString())
            }
        })
    }



}