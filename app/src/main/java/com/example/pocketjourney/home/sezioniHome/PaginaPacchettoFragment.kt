package com.example.pocketjourney.home.sezioniHome

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.R
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.database.DBManager
import com.example.pocketjourney.databinding.FragmentPaginaPacchettoBinding
import com.example.pocketjourney.home.PaginaPostoFragment
import com.example.pocketjourney.home.RecensioniFragment
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class PaginaPacchettoFragment : Fragment() {
    private lateinit var refRistorante:String
    private lateinit var refAttrazione:String
    private lateinit var refSoggiorno:String
    private lateinit var data:String
    private lateinit var numPersone:String
    private lateinit var oraRistorante:String
    private lateinit var oraAttrazione:String
    private lateinit var oraSoggiorno:String
    private lateinit var nomeRistorante:String
    private lateinit var nomeSoggiorno:String
    private lateinit var nomeAttrazione:String
    private var selectedRating:Float=0f

    private lateinit var binding: FragmentPaginaPacchettoBinding

    private var dbManager: DBManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentPaginaPacchettoBinding.inflate(inflater)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val idPacchetto = requireActivity().intent.getStringExtra("idPacchetto")
        requireActivity().intent.putExtra("frame","fragment_pagina_pacchetto")

        if (idUtente != null ){
            //sono connesso a internet avendo ricevuto userId ed interrogo il database remoto
            Log.e("Ciao","sei al pag pack fragment e sei connesso")

            val userAPI= ClientNetwork.retrofit
            val queryPopolaPacchetto = "SELECT * FROM Pacchetti WHERE idPacchetto = '$idPacchetto'"
            val call = userAPI.cerca(queryPopolaPacchetto)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.e("Ciao","posso poplare il pack")
                        val jsonObject = response.body() // Ottieni il JSON come JsonObject
                        Log.d("RISPOSTA QUERY:",jsonObject.toString())
                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {
                            Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset" )
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                val primoPack=querySetArray[0].asJsonObject //prendo la prima corrispondenza
                                Log.d("JSON", primoPack.toString())

                                //verifico che non sia null e che contenga i campi corretti

                                if (primoPack != null && primoPack.has("nome") && primoPack.has("descrizione") && primoPack.has("prezzo") && primoPack.has("foto") && primoPack.has("valutazione") && primoPack.has("numRecensioni") && primoPack.has("data")){
                                    //prelevo i campi e li setto nel fragment
                                    val nome=primoPack.get("nome").asString
                                    val descrizione=primoPack.get("descrizione").asString
                                    val numRec=primoPack.get("numRecensioni").asString
                                    val valutazione=primoPack.get("valutazione").asString
                                    val prezzo=primoPack.get("prezzo").asString
                                    data=primoPack.get("data").asString
                                    val foto=primoPack.get("foto").asString

                                    //il resto dei valori lo uso per passarli allìonviewCreated
                                    refRistorante=primoPack.get("refRistorante").asString
                                    refSoggiorno=primoPack.get("refHotel").asString
                                    refAttrazione=primoPack.get("refAttrazione").asString
                                    numPersone=primoPack.get("numPersone").asString
                                    oraRistorante=primoPack.get("oraRistorante").asString
                                    oraSoggiorno=primoPack.get("oraSoggiorno").asString
                                    oraAttrazione=primoPack.get("oraAttrazione").asString
                                    nomeRistorante=primoPack.get("nomeRistorante").asString
                                    nomeAttrazione=primoPack.get("nomeAttrazione").asString
                                    nomeSoggiorno=primoPack.get("nomeSoggiorno").asString

                                    binding.thirdTitle.text = "${nome}"
                                    binding.tvData.text = "${data}"
                                    binding.abutText.text="${descrizione}"
                                    binding.thirdRatingbarP.rating=valutazione.toFloat()
                                    binding.thirdRatingNumberP.setText(valutazione)
                                    binding.thirdRatingNumber2P.setText(numRec)
                                    binding.venueText.text="${prezzo}€"
                                    Log.e("Ciao", "HO CAMBIATO I DATI" )

                                    //setto l'immagine del profilo
                                    val downloadFotoPack=userAPI.getAvatar(foto)
                                    downloadFotoPack.enqueue(object :Callback<ResponseBody> {
                                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                            Log.e("Ciao", "sono dentro il blocco della foto" )
                                            Log.d("RESPONSE",response.isSuccessful.toString())
                                            if (response.isSuccessful){
                                                Log.e("Ciao", "sono dentro il blocco della foto DENTRO IS SUCCESSFULL" )
                                                val responseBody=response.body()
                                                if(responseBody!=null){
                                                    val inputStream=responseBody.byteStream()
                                                    val bitmap= BitmapFactory.decodeStream(inputStream)
                                                    //utilizza il Bitmap come immagine di profilo
                                                    binding.headerBackground.setImageBitmap(bitmap)

                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                            Toast.makeText(requireContext(),"L'immagine non è stata trovaa correttamente",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    })

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

        }


        binding.prenotaAdessoButton.setOnClickListener{
            val idUtente = requireActivity().intent.getStringExtra("idUtente")
            verificaCartaDiCredito(idUtente) { bool ->
                if (bool) {
                    // L'utente ha la carta di credito
                    // Esegui la funzione di inserimento prenotazione tre volte
                    inserisciPrenotazione(idUtente,refRistorante,data,numPersone,oraRistorante,nomeRistorante)
                    inserisciPrenotazione(idUtente,refSoggiorno,data,numPersone,oraSoggiorno,nomeSoggiorno)
                    inserisciPrenotazione(idUtente,refAttrazione,data,numPersone,oraAttrazione,nomeAttrazione)
                    Toast.makeText(requireContext(), "Prenotazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                } else {
                    // L'utente non ha la carta di credito
                    Toast.makeText(requireContext(), "Utente senza carta di credito", Toast.LENGTH_SHORT).show()
                }
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

            if (selectedRating!=0f && idUtente!=null && idPacchetto!=null){
                inserisciRecensione(idUtente,idPacchetto,titolo,testo,selectedRating,formattedDate)
                Log.d("sto passando a inserisci recensione:",idUtente+idPacchetto+titolo+testo+selectedRating+formattedDate)
            }
        }

        binding.scopriTutteLeRec2.setOnClickListener {
            val childFragment = RecensioniFragment()
            requireActivity().intent.putExtra("idPack",idPacchetto)
            requireActivity().intent.putExtra("idUtente",idUtente)
            requireActivity().intent.putExtra("provenienzaRec","pacchetto")
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_pagina_pacchetto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        binding.downArrowP.setOnClickListener{
            val childFragment = PacchettiFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_pagina_pacchetto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        return binding.root
    }





    fun verificaCartaDiCredito(idUtente: String?, callback: (Boolean) -> Unit) {
        val userAPI = ClientNetwork.retrofit
        val queryCartaPresente = "SELECT idDatiPagamento FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
        val call = userAPI.cerca(queryCartaPresente)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.e("Ciao","ho effettuato la query correttamente per cercare se utente ha gia una carta per poter pagare")
                    val jsonObject = response.body()
                    if (jsonObject != null && jsonObject.has("queryset")) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset" )
                        Log.d("ritorno dalla query: " , jsonObject.toString())
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size() > 0) {
                            Log.e("CIAO ", "UTENTE HA GIA UNA CARTA INSERITA")
                            callback(true) // Chiamata al callback con il valore true
                        } else {
                            Log.e("ciao","utente non ha carta non può pagare")
                            callback(false) // Chiamata al callback con il valore false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("Ciao"," ONFAILURE sulla ricerca della carta")
                callback(false) // Chiamata al callback con il valore false in caso di errore
            }
        })
    }




    private fun inserisciPrenotazione(idUtente: String?, idPosto: String?, dataPrenotazione: String, numPersone: String, ora: String,nome:String) {
        //dati utili per l'inserimetno in locale
        val email= requireActivity().intent.getStringExtra("emailOnline")
        Log.d("DATI OFFLINE=",email + " " + nome)
        val userAPI = ClientNetwork.retrofit
        val queryinserisciPrenotazione = "INSERT INTO Prenotazioni (ref_utente, ref_posto, data, numPersone, orario) VALUES ('$idUtente','$idPosto','$dataPrenotazione','$numPersone','$ora')"
        val call = userAPI.inserisci(queryinserisciPrenotazione)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    // L'inserimento della carta è avvenuto
                    Log.e("ciao","Prenotazione INSERITA")
                    dbManager = context?.let { it2 -> DBManager(it2) }
                    if(dbManager!=null) {
                        dbManager?.open()
                        if (email != null && nome != null) {
                            dbManager?.insertPrenotazione(
                                email,
                                nome,
                                dataPrenotazione,
                                numPersone,
                                ora,
                            )
                        }
                    }
                    Toast.makeText(requireContext(), "Prenotazione avvenuta con successo!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
                //Toast.makeText(requireContext(), t.toString() + " " + t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("ciao",t.toString() + " " + t.message.toString())
            }
        })
    }

    private fun inserisciRecensione(
        idUtente: String,
        idPacchetto: String,
        titolo: String,
        testo: String,
        rating: Float,
        formattedDate: String
    ) {
        Log.d("DATI OFFLINE=",idPacchetto + " " + idUtente + " " + titolo + " " + rating.toString() + " "+ formattedDate + " " + testo + " "+ rating)
        val userAPI = ClientNetwork.retrofit
        val queryinserisciRecensione = "INSERT INTO RecensioniPacchetti (ref_utente, ref_pacchetto, titolo, testo, valutazione, data) VALUES ('$idUtente', '$idPacchetto', '$titolo','$testo','$rating','$formattedDate')"
        val call = userAPI.inserisci(queryinserisciRecensione)
        Log.d("query:",queryinserisciRecensione)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    // L'inserimento della carta è avvenuto
                    Log.e("ciao","Recensione inserita")
                    Toast.makeText(requireContext(), "Recensione inserita con successo!", Toast.LENGTH_SHORT).show()
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