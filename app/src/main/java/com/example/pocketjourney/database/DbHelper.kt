package com.example.pocketjourney.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper (context: Context): SQLiteOpenHelper (context, DB_NAME,null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_UTENTE)
        db?.execSQL(SQL_CREATE_PRENOTAZIONI)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_CREATE_UTENTE)
        db?.execSQL(SQL_CREATE_PRENOTAZIONI)
        onCreate(db)
    }

    companion object {
        //nome db
        const val DB_NAME="UtentiDB"

        //nome tabella
        const val TABLE_NAME="Utenti"

        //colonne
        const val _ID="id"
        const val NOME:String="Nome"
        const val COGNOME="Cognome"
        const val EMAIL:String="email"
        const val PASSWORD:String="password"
        const val NUMEROCELLULARE = "`NumeroDiTelefono`"
        const val REF_DATI_PAGAMENTO="DatiPagamento"
        const val REF_RECENSISCE="Recensioni"
        const val REF_PRENOTA="Prenotazioni"



        //versione del db
        const val DB_VERSION=2

        //stringhe per creare la tabella
        private const val SQL_CREATE_UTENTE =
            "CREATE TABLE $TABLE_NAME ( " +
                    "$_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$NOME TEXT NOT NULL," +
                    "$COGNOME TEXT NOT NULL,"+
                    "$EMAIL TEXT NOT NULL," +
                    "$PASSWORD TEXT NOT NULL," +
                    "$NUMEROCELLULARE TEXT NOT NULL);"

        //stringhe per eliminare la tabella
        private const val SQL_DELETE_UTENTE =
            "DROP TABLE IF EXISTS $TABLE_NAME;"


        //TABELLA PRENOTAZIONI
        const val TABLE_PRENOTAZIONI = "Prenotazioni"
        const val _ID_PRENOTAZIONI = "idPrenotazioni"
        const val EMAIL_UTENTE = "email_utente"
        const val NOME_POSTO = "nome_posto"
        const val DATA = "data"
        const val NUM_PERSONE = "numPersone"
        const val ORARIO = "orario"

        // Aggiungi la stringa per creare la tabella Prenotazioni
        private const val SQL_CREATE_PRENOTAZIONI =
            "CREATE TABLE $TABLE_PRENOTAZIONI (" +
                    "$_ID_PRENOTAZIONI INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$EMAIL_UTENTE TEXT NOT NULL," +
                    "$NOME_POSTO TEXT NOT NULL," +
                    "$DATA TEXT NOT NULL," +
                    "$NUM_PERSONE TEXT NOT NULL," +
                    "$ORARIO TEXT NOT NULL);"

        // Aggiungi la stringa per eliminare la tabella Prenotazioni
        private const val SQL_DELETE_PRENOTAZIONI =
            "DROP TABLE IF EXISTS $TABLE_PRENOTAZIONI;"
    }

}

