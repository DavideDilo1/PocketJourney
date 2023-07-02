package com.example.pocketjourney.home


import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.pocketjourney.R
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentAnteprimaPostoBinding
import com.example.pocketjourney.home.sezioniHome.AttrazioniFragment
import com.example.pocketjourney.home.sezioniHome.HotelFragment
import com.example.pocketjourney.home.sezioniHome.PaginaConsigliatiFragment
import com.example.pocketjourney.home.sezioniHome.RistorantiFragment
import com.example.pocketjourney.preferiti.ListaPreferitiFragment
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnteprimaPostoFragment : Fragment() {
    private lateinit var binding: FragmentAnteprimaPostoBinding


    private lateinit var second_back_arrow: ImageView
    private lateinit var second_arrow_up: ImageView
    private lateinit var second_title: TextView
    private lateinit var second_subtitle: TextView
    private lateinit var second_rating_number: TextView
    private lateinit var second_rating_number2: TextView
    private lateinit var more_details: TextView
    private lateinit var second_ratingbar: RatingBar

    private lateinit var queryResult: JsonObject


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAnteprimaPostoBinding.inflate(inflater)


        val idPosto = requireActivity().intent.getStringExtra("idPosto")
        val scope = CoroutineScope(Dispatchers.Default)
        requireActivity().intent.putExtra("frame","fragment_anteprima_posto")



        val userAPI = ClientNetwork.retrofit
        val queryMostraAnteprima = "SELECT * FROM Posti WHERE idPosti = '$idPosto'"
        val call = userAPI.cerca(queryMostraAnteprima)
        scope.launch{
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObject = response.body() // Ottieni il JSON come JsonObject

                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset")) {
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size() > 0) {
                            val primoPosto =
                                querySetArray[0].asJsonObject //prendo la prima corrispondenza

                            //verifico che non sia null e che contenga i campi corretti

                            if (primoPosto != null && primoPosto.has("nome") && primoPosto.has("citta") && primoPosto.has(
                                    "paese"
                                ) && primoPosto.has("categoria") && primoPosto.has("tipologia") && primoPosto.has(
                                    "descrizione"
                                ) && primoPosto.has("prezzo") && primoPosto.has("valutazione") && primoPosto.has(
                                    "numRecensioni"
                                ) && primoPosto.has("foto")
                            ) {
                                //prelevo i campi e li setto nel fragment
                                queryResult = primoPosto
                                val nome = primoPosto.get("nome").asString
                                val descrizione = primoPosto.get("descrizione").asString
                                val valutazione = primoPosto.get("valutazione").asString
                                val rec = primoPosto.get("numRecensioni").asString
                                val foto = primoPosto.get("foto").asString

                                binding.secondSubtitle.text = "${descrizione}"
                                binding.secondTitle.text = "${nome}"
                                binding.secondRatingBar.rating = valutazione.toFloat()
                                binding.secondRatingNumber.text = "${valutazione}"
                                binding.secondRatingNumber2.text = "${rec}"

                                //setto l'immagine del profilo
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
                                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                                //utilizza il Bitmap come immagine di profilo
                                                binding.imageBackgroundAnteprima.setImageBitmap(
                                                    bitmap
                                                )

                                            }
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        Toast.makeText(
                                            requireContext(),
                                            "L'immagine non è stata trovaa correttamente",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })

                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
            }
        })

    }





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
                "preferiti" -> ListaPreferitiFragment()
                else -> HomeFragmentNew()
            }
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_anteprima_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        second_arrow_up.setOnClickListener{
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

