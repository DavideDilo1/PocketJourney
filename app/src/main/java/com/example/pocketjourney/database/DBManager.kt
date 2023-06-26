package com.example.pocketjourney.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log

class DBManager (val context: Context) {
    private lateinit var helper: DbHelper
    private lateinit var db:SQLiteDatabase

    fun open(): DBManager {
        helper= DbHelper(context)
        db=helper.writableDatabase
        return this
    }

    fun close(){
        helper.close()
    }

    fun insert_utente(nome:String,cognome:String,email:String,pw:String,telefono:String){
        val values=ContentValues().apply {
            put(DbHelper.NOME,nome)
            put(DbHelper.COGNOME,cognome)
            put(DbHelper.EMAIL,email)
            put(DbHelper.PASSWORD,pw)
            put(DbHelper.NUMEROCELLULARE,telefono)
        }
        val newRowId=db.insert(DbHelper.TABLE_NAME,null,values)
    }

    fun updateUtente(_id:Long,nome:String,cognome:String,email:String,pw:String,cell:String):Int{
        val selezione= "${DbHelper._ID}=?"
        val selectionArgs=arrayOf(_id.toString())

        val values=ContentValues().apply {
            put(DbHelper.EMAIL,email)
            put(DbHelper.PASSWORD,pw)
            put(DbHelper.NUMEROCELLULARE,cell)
        }
        val rit=db.update(DbHelper.TABLE_NAME,values,selezione,selectionArgs)
        return rit
    }

    @SuppressLint("Range")
    fun updateEmailUtente(nuovaEmail:String, vecchiaEmail:String):Int{
        val query = "SELECT id FROM Utenti WHERE email = ?"
        val selectionArgs = arrayOf(vecchiaEmail)
        val cursor = db.rawQuery(query, selectionArgs)

        var idUtente: Int? = null

        if (cursor.moveToFirst()) {
            idUtente = cursor.getInt(cursor.getColumnIndex("idUtente"))


            cursor.close()

// Restituisci l'ID dell'utente corrispondente all'email specificata

            /* val selezione = "${DbHelper.EMAIL}=?"
         val selectionArgs = arrayOf(vecchiaEmail) // Vecchia email
         Log.d("selezione:",selezione)
         Log.d("vecchiaemali:",vecchiaEmail)
         Log.d("nuovamail:",nuovaEmail)
         val values = ContentValues().apply {
             put(DbHelper.EMAIL, nuovaEmail) // Nuova email
         }

         val righeModificate = db.update(DbHelper.TABLE_NAME, values, selezione, selectionArgs)
         Log.d("righe mod", righeModificate.toString())
         db.close()

         return righeModificate*/
            val query2 = "UPDATE Utenti SET email='${nuovaEmail}' WHERE id='${idUtente}'"
            Log.d("query", query2)
            db.execSQL(query)
        }
        return 1
    }
    fun updatePasswordUtente(password:String):Int{
        val selezione= "${DbHelper.PASSWORD}=?"
        val selectionArgs=arrayOf(password.toString())

        val values=ContentValues().apply {
            put(DbHelper.PASSWORD,password)
        }
        val rit=db.update(DbHelper.TABLE_NAME,values,selezione,selectionArgs)
        return rit
    }
    fun updateTelefonoUtente(cellulare:String):Int{
        val selezione= "${DbHelper.NUMEROCELLULARE}=?"
        val selectionArgs=arrayOf(cellulare.toString())

        val values=ContentValues().apply {
            put(DbHelper.NUMEROCELLULARE,cellulare)
        }
        val rit=db.update(DbHelper.TABLE_NAME,values,selezione,selectionArgs)
        return rit
    }

    fun deleteUtente(_id: Long) {
        val selection = "${DbHelper._ID}=? "
        val selectionArgs = arrayOf(_id.toString())
        db.delete(DbHelper.TABLE_NAME, selection, selectionArgs)
    }

    //con la fetchAll ottengo tutte le risposte di una query
    fun fetchAll():Cursor {
        val projection = arrayOf(
            DbHelper._ID,
            DbHelper.NOME,
            DbHelper.COGNOME,
            DbHelper.EMAIL,
            DbHelper.PASSWORD,
            DbHelper.NUMEROCELLULARE,
            DbHelper.REF_DATI_PAGAMENTO,
            DbHelper.REF_RECENSISCE,
            DbHelper.REF_PRENOTA
        )
        val cursor = db.query(
            DbHelper.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        cursor?.moveToFirst()
        return cursor
    }
}