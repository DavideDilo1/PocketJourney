package com.example.pocketjourney.home

import android.annotation.TargetApi
import android.graphics.BitmapFactory
import androidx.core.util.Pair
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.pocketjourney.R
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentAnteprimaPostoBinding
import com.example.pocketjourney.home.sezioniHome.AttrazioniFragment
import com.example.pocketjourney.home.sezioniHome.HotelFragment
import com.example.pocketjourney.home.sezioniHome.PaginaConsigliatiFragment
import com.example.pocketjourney.home.sezioniHome.RistorantiFragment
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnteprimaPostoFragment : Fragment() {
    private var id: Int = 0
    private lateinit var binding: FragmentAnteprimaPostoBinding


    private lateinit var second_back_arrow: ImageView
    private lateinit var second_arrow_up: ImageView
    private lateinit var second_title: TextView
    private lateinit var second_subtitle: TextView
    private lateinit var second_rating_number: TextView
    private lateinit var second_rating_number2: TextView
    private lateinit var more_details: TextView
    private lateinit var second_ratingbar: RatingBar

    private lateinit var from_left: Animation
    private lateinit var from_right: Animation
    private lateinit var from_bottom: Animation
    private lateinit var queryResult: JsonObject


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAnteprimaPostoBinding.inflate(inflater)


        val idPosto = requireActivity().intent.getStringExtra("idPosto")
        val idUtente = requireActivity().intent.getStringExtra("idUtente")

        Log.d("Sono anteprima e ho ricevuto" , idPosto.toString() + " e" + idUtente.toString() )


        val userAPI= ClientNetwork.retrofit
        val queryMostraAnteprima = "SELECT * FROM Posti WHERE idPosti = '$idPosto'"
        val call = userAPI.cerca(queryMostraAnteprima)
        call.enqueue(object : Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.e("Ciao","sto mostrando anteprima")
                    val jsonObject = response.body() // Ottieni il JSON come JsonObject

                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset" )
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size()>0){
                            val primoPosto=querySetArray[0].asJsonObject //prendo la prima corrispondenza
                            Log.d("JSON", primoPosto.toString())

                            //verifico che non sia null e che contenga i campi corretti

                            if (primoPosto != null && primoPosto.has("nome") && primoPosto.has("citta") && primoPosto.has("paese") && primoPosto.has("categoria") && primoPosto.has("tipologia") && primoPosto.has("descrizione") && primoPosto.has("prezzo") && primoPosto.has("valutazione") && primoPosto.has("numRecensioni") && primoPosto.has("foto")) {
                                //prelevo i campi e li setto nel fragment
                                queryResult = primoPosto
                                Log.d("oggetto che passo al paginaposto", queryResult.toString())
                                val nome=primoPosto.get("nome").asString
                                val descrizione=primoPosto.get("descrizione").asString
                                val valutazione=primoPosto.get("valutazione").asString
                                val rec=primoPosto.get("numRecensioni").asString
                                val foto=primoPosto.get("foto").asString

                                binding.secondSubtitle.text = "${descrizione}"
                                binding.secondTitle.text = "${nome}"
                                binding.secondRatingBar.rating=valutazione.toFloat()
                                binding.secondRatingNumber.text="${valutazione}"
                                binding.secondRatingNumber2.text="${rec}"

                                Log.e("Ciao", "HO CAMBIATO I DATI" )

                                //setto l'immagine del profilo
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
                                                binding.imageBackgroundAnteprima.setImageBitmap(bitmap)

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

        /*
        else{
            //TODO: SCHERMATA NESSUN DATO DISPONIBILE
        }*/






        second_arrow_up = binding.secondArrowUp
        second_back_arrow = binding.secondBackArrow
        second_title = binding.secondTitle
        second_subtitle = binding.secondSubtitle
        second_rating_number = binding.secondRatingNumber
        second_ratingbar = binding.secondRatingBar
        more_details = binding.moreDetails
        second_rating_number2 = binding.secondRatingNumber2

        second_back_arrow.setOnClickListener{
            val provenienza = requireActivity().intent.getStringExtra("provenienza")
            val childFragment: Fragment = when (provenienza) {
                "ristorantiFragment" -> RistorantiFragment()
                "hotelFragment" -> HotelFragment()
                "attrazioniFragment" -> AttrazioniFragment()
                "consigliati" -> PaginaConsigliatiFragment()
                else -> HomeFragmentNew()
            }
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_anteprima_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }



        requireActivity().window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }


        from_left = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_left)
        from_right = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_right)
        from_bottom = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_from_bottom)

        second_back_arrow.setAnimation(from_left)
        second_title.setAnimation(from_right)
        second_subtitle.setAnimation(from_right)
        second_ratingbar.setAnimation(from_left)
        second_rating_number.setAnimation(from_right)
        second_rating_number2.setAnimation(from_left)
        second_arrow_up.setAnimation(from_bottom)
        more_details.setAnimation(from_bottom)

        second_arrow_up.setOnClickListener{
            Log.d("hai cliccato arrow up e passo",queryResult.toString() )
            val childFragment = PaginaPostoFragment()
            requireActivity().intent.putExtra("queryResult",queryResult.toString())
            // Assumi che tu stia eseguendo questo codice all'interno di un'attività
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_anteprima_posto, childFragment) // R.id.fragment_container rappresenta l'ID del contenitore del frammento nel layout dell'attività
            fragmentTransaction.addToBackStack(null) // Aggiunge il frammento alla pila retrostante per poter tornare indietro se necessario
            fragmentTransaction.commit()
        }






        return binding.root;
    }


}

