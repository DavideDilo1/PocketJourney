package com.example.pocketjourney

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.databinding.FragmentCreditCardBinding
import com.example.pocketjourney.databinding.FragmentModificaDatiBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreditCardFragment : Fragment() {
    private lateinit var binding: FragmentCreditCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCreditCardBinding.inflate(layoutInflater,container,false)
        //alla creazione del fragment carico i dati presenti nel db se ci sono
        val idUtente = arguments?.getString("idUtente")
        val userAPI=ClientNetwork.retrofit
        val queryDatiCarta = "SELECT idDatiPagamento, ref_IdUtente, numeroCarta, codiceSicurezza, meseScadenza, annoScadenza FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
        val call = userAPI.cerca(queryDatiCarta)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //posso effettuare il login online
                    Log.e("Ciao","ho effettuato la query correttamente ")
                    val jsonObject = response.body() // Ottieni il JSON come JsonObject

                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        Log.e("Ciao", "HO OTTENUTO IL JSONOBJECT come queryset" )
                        Log.d("ritorno dalla query: " , jsonObject.toString())
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size()>0){
                            val primaCarta=querySetArray[0].asJsonObject //prendo la prima corrispondenza
                            Log.d("JSON", primaCarta.toString())

                            //verifico che non sia null e che contenga i campi corretti

                            if (primaCarta!=null && primaCarta.has("numeroCarta") && primaCarta.has("codiceSicurezza") && primaCarta.has("meseScadenza") && primaCarta.has("annoScadenza")){
                                //prelevo i campi e li setto nel fragment
                                val numCarta=primaCarta.get("numeroCarta").asString
                                val meseScadenza=primaCarta.get("meseScadenza").asString
                                val annoScadenza=primaCarta.get("annoScadenza").asString

                                binding.tvNumeroCarta.text = "Numero carta: ${numCarta}"
                                binding.tvDataScadenza.text= " ${meseScadenza} / ${annoScadenza}"
                                Log.e("Ciao", "HO mostrato I DATI della tua carta" )
                            }
                        }
                        else {
                            Log.e("ciao ", "LA QUERY è VUOTA PORCODDIOOOOOOO")
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idUtente = arguments?.getString("idUtente")
        Log.d("ciao ho ottenuto ",idUtente.toString())

        binding.btnInserisciCarta.setOnClickListener{

        }

        binding.btnEliminaCarta.setOnClickListener{

        }

        binding.btnTornaProfilo.setOnClickListener {

        }
    }

}