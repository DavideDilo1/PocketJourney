package com.example.pocketjourney.profilo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pocketjourney.ProfileFragment
import com.example.pocketjourney.R
import com.example.pocketjourney.database.ClientNetwork
import com.example.pocketjourney.databinding.FragmentCreditCardBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CreditCardFragment : Fragment() {
    private lateinit var binding: FragmentCreditCardBinding
    private var formCardIsShowing= false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCreditCardBinding.inflate(layoutInflater,container,false)
        //alla creazione del fragment carico i dati presenti nel db se ci sono
        val idUtente = requireActivity().intent.getStringExtra("idUtente")
        requireActivity().intent.putExtra("frame","FragmentCartaDiCredito")
        val userAPI= ClientNetwork.retrofit
        val queryDatiCarta = "SELECT idDatiPagamento, ref_IdUtente, numeroCarta, codiceSicurezza, meseScadenza, annoScadenza FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
        val call = userAPI.cerca(queryDatiCarta)


        binding.buttonMostraFormCarta.setOnClickListener{
            if(formCardIsShowing){
                binding.etNumeroCarta.visibility = View.VISIBLE
                binding.etCvv.visibility = View.VISIBLE
                binding.tvAnnoScadenza.visibility = View.VISIBLE
                binding.tvMeseScadenza.visibility = View.VISIBLE
                binding.yearSpinner.visibility = View.VISIBLE
                binding.monthSpinner.visibility = View.VISIBLE
                binding.btnInserisciCarta.visibility =View.VISIBLE

            }else{
                binding.etNumeroCarta.visibility = View.GONE
                binding.etCvv.visibility = View.GONE
                binding.tvAnnoScadenza.visibility = View.GONE
                binding.tvMeseScadenza.visibility = View.GONE
                binding.yearSpinner.visibility = View.GONE
                binding.monthSpinner.visibility = View.GONE
                binding.btnInserisciCarta.visibility =View.GONE

            }


            formCardIsShowing = !formCardIsShowing
        }



        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    //il collegamento è avvenuto correttamente
                    val jsonObject = response.body() // Ottieni il JSON come JsonObject

                    // Verifica se il JSON object è stato ottenuto correttamente come queryset
                    if (jsonObject != null && jsonObject.has("queryset") ) {
                        //salvo l'array e verifico che contenga almeno un elemento
                        val querySetArray = jsonObject.getAsJsonArray("queryset")
                        if (querySetArray != null && querySetArray.size()>0){
                            val primaCarta=querySetArray[0].asJsonObject //prendo la prima corrispondenza

                            //verifico che non sia null e che contenga i campi corretti

                            if (primaCarta!=null && primaCarta.has("numeroCarta") && primaCarta.has("codiceSicurezza") && primaCarta.has("meseScadenza") && primaCarta.has("annoScadenza")){
                                //prelevo i campi e li setto nel fragment
                                val numCarta=primaCarta.get("numeroCarta").asString
                                val meseScadenza=primaCarta.get("meseScadenza").asString
                                val annoScadenza=primaCarta.get("annoScadenza").asString

                                binding.creditCardNumber.text = formatCreditCardNumber("${numCarta}")
                                binding.expireCreditCard.text= "${meseScadenza}/${annoScadenza}"
                            }
                        }
                        else {
                            //l'interrogazione ha dato un risultato nullo quindi non faccio niente
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Si è verificato un errore durante la chiamata di rete online
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val idUtente = requireActivity().intent.getStringExtra("idUtente")

        binding.btnInserisciCarta.setOnClickListener{

            val numeroCarta=binding.etNumeroCarta.text.toString()
            val meseScadenza=binding.monthSpinner.selectedItem.toString().toInt()
            val annoScadenza=binding.yearSpinner.selectedItem.toString().toInt()
            val CVV=binding.etCvv.text.toString()

            //effettuo una query per verificare che un utente non abbia già una carta inserita
            val userAPI= ClientNetwork.retrofit
            val queryCartaPresente = "SELECT idDatiPagamento FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
            val call = userAPI.cerca(queryCartaPresente)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        //il collegamento è avvenuto correttamente
                        val jsonObject = response.body() // Ottieni il JSON come JsonObject

                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                Toast.makeText(requireContext(), "Elimina la carta corrente prima di inserirne una nuova.", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                //l'interrogazione ha dato un risultato nullo
                                if(verificaDatiInseriti(numeroCarta,meseScadenza,annoScadenza,CVV)){
                                    val queryInserisciCarta = "insert into DatiPagamento(ref_IdUtente,numeroCarta,codiceSicurezza,meseScadenza,annoScadenza) values('$idUtente','$numeroCarta','$CVV','$meseScadenza','$annoScadenza')"
                                    val call = userAPI.inserisci(queryInserisciCarta)
                                    call.enqueue(object : Callback<JsonObject> {
                                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                            if (response.isSuccessful) {
                                                // L'inserimento della carta è avvenuto
                                                Toast.makeText(requireContext(), "Registrazione CARTA avvenuta con successo!", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                            // Si è verificato un errore durante la chiamata di rete online
                                            //Toast.makeText(requireContext(), t.toString() + " " + t.message.toString(), Toast.LENGTH_SHORT).show()
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
                }
            })

        }

        binding.btnEliminaCarta.setOnClickListener{

            //effettuo una query per verificare che un utente non abbia già una carta inserita
            val userAPI= ClientNetwork.retrofit
            val queryCartaPresente = "SELECT idDatiPagamento FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
            val call = userAPI.cerca(queryCartaPresente)
            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        //il collegamento è avvenuto correttamente
                        val jsonObject = response.body() // Ottieni il JSON come JsonObject

                        // Verifica se il JSON object è stato ottenuto correttamente come queryset
                        if (jsonObject != null && jsonObject.has("queryset") ) {
                            //salvo l'array e verifico che contenga almeno un elemento
                            val querySetArray = jsonObject.getAsJsonArray("queryset")
                            if (querySetArray != null && querySetArray.size()>0){
                                //l'interrogazione ha dato un risultato nullo

                                val queryEliminaCarta = "DELETE FROM DatiPagamento WHERE ref_IdUtente = '$idUtente'"
                                val call = userAPI.remove(queryEliminaCarta)
                                call.enqueue(object : Callback<JsonObject> {
                                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                            if (response.isSuccessful) {
                                                // L'inserimento della carta è avvenuto
                                                binding.creditCardNumber.text = "XXXX XXXX XXXX XXXX"
                                                binding.expireCreditCard.text= " --/-- "
                                                Toast.makeText(context, "Hai rimosso la tua carta con successo e puoi inserirne una nuova", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                            // Si è verificato un errore durante la chiamata di rete online
                                            //Toast.makeText(requireContext(), t.toString() + " " + t.message.toString(), Toast.LENGTH_SHORT).show()
                                        }
                                    })

                            }
                            else {
                                Toast.makeText(requireContext(), "Nessuna carta da rimuovere trovata. ", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Si è verificato un errore durante la chiamata di rete online
                    //login in locale

                }
            })

        }

        binding.btnTornaProfilo.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameHomeLayout, ProfileFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    fun verificaNumeroCarta(numero: String): Boolean {
        val numeroRegex = Regex("^[0-9]{16}$")
        val isNumeroValido = numero.matches(numeroRegex)
        if (!isNumeroValido) {
            Toast.makeText(context, "Inserisci un numero di carta valido (16 cifre)", Toast.LENGTH_SHORT).show()
        }
        return isNumeroValido
    }

    fun verificaAnnoScadenza(anno: Int): Boolean {
        val annoCorrente = Calendar.getInstance().get(Calendar.YEAR)
        if (anno < annoCorrente) {
            Toast.makeText(context, "L'anno di scadenza deve essere maggiore o uguale all'anno corrente", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun verificaCVV(cvv: String): Boolean {
        val cvvRegex = Regex("^[0-9]{3}$")
        val isCVVValido = cvv.matches(cvvRegex)
        if (!isCVVValido) {
            Toast.makeText(context, "Inserisci un CVV valido (3 cifre)", Toast.LENGTH_SHORT).show()
        }
        return isCVVValido
    }

    fun verificaDatiInseriti(
        numeroCarta: String,
        meseScadenza: Int,
        annoScadenza: Int,
        cvv: String
    ): Boolean {
        if (numeroCarta.isEmpty() || cvv.isEmpty() || meseScadenza==null || annoScadenza== null) {
            Toast.makeText(context, "Riempi tutti i campi ", Toast.LENGTH_SHORT).show()
            return false
        }
        return (verificaNumeroCarta(numeroCarta)
                && verificaAnnoScadenza(annoScadenza)
                && verificaCVV(cvv) )
    }

    private fun formatCreditCardNumber(input: String): String {
        val cardNumber = input.replace(" ", "")
        val formattedText = StringBuilder()

        for (i in cardNumber.indices) {
            if (i > 0 && i % 4 == 0) {
                formattedText.append(" ")
            }
            formattedText.append(cardNumber[i])
        }

        return formattedText.toString()
    }
}
