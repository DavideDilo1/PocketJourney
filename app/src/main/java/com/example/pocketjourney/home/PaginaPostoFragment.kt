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
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ScrollView
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


class PaginaPostoFragment : Fragment() {
    private lateinit var binding: FragmentPaginaPostoBinding
    private lateinit var down_arrow: ImageView
    private lateinit var from_bottom: Animation


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
                    )
        }


        down_arrow.setOnClickListener{
            val pairs = arrayOf<Pair<View, String>>(
                Pair(down_arrow, "background_image_transition")
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), *pairs).toBundle()

            val childFragment = AnteprimaPostoFragment()
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            childFragment.arguments = options
            fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment)
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
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_pagina_posto, childFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }





        return binding.root
    }

}