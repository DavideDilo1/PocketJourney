package com.example.pocketjourney

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper (context: Context): SQLiteOpenHelper (context,DB_NAME,null,DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_UTENTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_UTENTE)
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
        const val PASSWORD:String="Password"
        const val NUMEROCELLULARE:String="Numero di telefono"
        const val REF_DATI_PAGAMENTO="Dati Pagamento"
        const val REF_RECENSISCE="Recensioni"
        const val REF_PRENOTA="Prenotazioni"



        //versione del db
        const val DB_VERSION=1

        //stringhe per creare la tabella
        private const val SQL_CREATE_UTENTE =
            "CREATE TABLE ${TABLE_NAME} ( " +
                    "$_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$NOME TEXT NOT NULL," +
                    "$COGNOME TEXT NOT NULL,"+
                    "$EMAIL TEXT NOT NULL," +
                    "$PASSWORD TEXT NOT NULL," +
                    "$NUMEROCELLULARE TEXT NOT NULL," +
                    "$REF_DATI_PAGAMENTO TEXT, " +
                    "$REF_RECENSISCE TEXT, " +
                    "$REF_PRENOTA TEXT);"

        //stringhe per eliminare la tabella
        private const val SQL_DELETE_UTENTE =
            "DROP TABLE IF EXISTS ${TABLE_NAME};"

    }

}