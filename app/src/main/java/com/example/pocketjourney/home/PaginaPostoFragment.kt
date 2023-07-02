package com.example.pocketjourney.home

import android.annotation.TargetApi
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.pocketjourney.R
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentPaginaPostoBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class PaginaPostoFragment : Fragment() {
    private lateinit var binding: FragmentPaginaPostoBinding
    private lateinit var down_arrow: ImageView
    private lateinit var from_bottom: Animation
    private var selectedRating:Float=0f

    @TargetApi(Build.VERSION_CODES.S)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaginaPostoBinding.inflate(inflater)

        val queryResultString = requireActivity().intent.getStringExtra("queryResult")
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        val idPosto= requireActivity().intent.getStringExtra("idPosto")
        Log.d("SONO PAGINA POSTO E HO RICEVUTO ", queryResultString.toString() + " " + idUtente)
        requireActivity().intent.putExtra("frame","fragment_pagina_posto")
        val email = requireActivity().intent.getStringExtra("email")
        if (queryResultString != null) {
            val jsonParser = JsonParser()
            val queryResult = jsonParser.parse(queryResultString).asJsonObject

            // Recupero tutti i valori della query
            val nome = queryResult.get("nome")?.asString!!
            val citta = queryResult.get("citta")?.asString!!
            val paese = queryResult.get("paese")?.asString!!
            val categoria = queryResult.get("categoria")?.asString!!
            val tipologia = queryResult.get("tipologia")?.asString!!
            val descrizione = queryResult.get("descrizione")?.asString!!
            val prezzo = queryResult.get("prezzo")?.asString!!
            val valutazione = queryResult.get("valutazione")?.asString!!
            val numRecensioni = queryResult.get("numRecensioni")?.asString!!
            val foto = queryResult.get("foto")?.asString!!

            // Continuo con l'utilizzo dei valori ottenuti

            //per prima cosa setto la foto
            val userAPI= ClientNetwork.retrofit
            val downloadFotoPosto=userAPI.getAvatar(foto)
            downloadFotoPosto.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.e("Ciao", "sono dentro il blocco della foto dell'anteprima" )
                    Log.d("RESPONSE",response.isSuccessful.toString())
                    if (response.isSuccessful){
                        Log.e("Ciao", "sono dentro il blocco della foto  anteprima DENTRO IS SUCCESSFULL" )
                        val responseBody=response.body()
                        if(responseBody!=null){
                            val inputStream=responseBody.byteStream()
                            val bitmap= BitmapFactory.decodeStream(inputStream)
                            //utilizza il Bitmap come immagine di profilo
                            binding.headerBackground.setImageBitmap(bitmap)

                        }
                        //ora setto il resto dei valori
                        binding.thirdTitle.setText(nome)
                        binding.thirdRatingbar.rating=valutazione.toFloat()
                        binding.thirdRatingNumber.setText(valutazione)
                        binding.thirdRatingNumber2.setText(numRecensioni)
                        binding.abutText.setText(descrizione)
                        binding.venueType.text = "${categoria},${tipologia}"
                        binding.venueText.text= "${prezzo}€"
                        binding.viewPlace.text= "${citta},${paese}"
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(requireContext(),"L'immagine non è stata trovaa correttamente",
                        Toast.LENGTH_SHORT).show()
                }
            })

        }




        down_arrow = binding.downArrow

        from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)

        down_arrow.setAnimation(from_bottom)


        down_arrow.setOnClickListener{
            val childFragment = AnteprimaPostoFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment).remove(this)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }

            binding.prenotaAdessoButton.setOnClickListener{
                val childFragment = BookingFragment()
                requireActivity().intent.putExtra("idPosto",idPosto)
                requireActivity().intent.putExtra("idUtente",idUtente)
                val jsonParser = JsonParser()
                val queryResult = jsonParser.parse(queryResultString).asJsonObject
                val categoria = queryResult.get("categoria")?.asString!!
                val nomePosto= queryResult.get("nome").asString!!
                requireActivity().intent.putExtra("categoria",categoria)
                requireActivity().intent.putExtra("nomePosto",nomePosto)
                requireActivity().intent.putExtra("emailOnline",email)
                val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }

        //TODO: AGGIUNGERE IL CORRETTO CAMBIO DI FRAGMENT
        binding.scopriTutteLeRec.setOnClickListener {
            val childFragment = RecensioniFragment()
            requireActivity().intent.putExtra("idPosto",idPosto)
            requireActivity().intent.putExtra("provenienzaRec","postoSemplice")
            requireActivity().intent.putExtra("idUtente",idUtente)
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
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
                inserisciRecensione(idUtente,idPosto,titolo,testo,selectedRating,formattedDate)
            }
        }




        return binding.root
    }

    private fun inserisciRecensione(
        idUtente: String,
        idPosto: String,
        titolo: String,
        testo: String,
        rating: Float,
        formattedDate: String
    ) {
            //dati utili per l'inserimetno in locale
            Log.d("DATI OFFLINE=",idPosto + " " + idUtente + " " + titolo + " " + rating.toString() + " "+ formattedDate + " " + testo + " "+ rating)
            val userAPI = ClientNetwork.retrofit
            val queryinserisciRecensione = "INSERT INTO RecensioniPosti (ref_utente, ref_posto, titolo, testo, valutazione, data) VALUES ('$idUtente', '$idPosto', '$titolo','$testo','$rating','$formattedDate')"
            val call = userAPI.inserisci(queryinserisciRecensione)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        // L'inserimento della carta è avvenuto
                        Log.e("ciao","Recensione inserita")
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