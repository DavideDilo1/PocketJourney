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

    fun insertPrenotazione(emailUtente: String, nomePosto: String, data: String, numPersone: String, orario: String) {
        val values = ContentValues().apply {
            put(DbHelper.EMAIL_UTENTE, emailUtente)
            put(DbHelper.NOME_POSTO, nomePosto)
            put(DbHelper.DATA, data)
            put(DbHelper.NUM_PERSONE, numPersone)
            put(DbHelper.ORARIO, orario)
        }
        val newRowId = db.insert(DbHelper.TABLE_PRENOTAZIONI, null, values)
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
    fun updateEmailUtente(nuovaEmail: String, vecchiaEmail: String): Int {
        val selezione = "${DbHelper.EMAIL}=?"
        val selectionArgs = arrayOf(vecchiaEmail)

        val values = ContentValues().apply {
            put(DbHelper.EMAIL, nuovaEmail)
        }

        val rit = db.update(DbHelper.TABLE_NAME, values, selezione, selectionArgs)

        return rit
    }

    fun updatePasswordUtente(email: String, newPassword: String): Int {
        val selezione = "${DbHelper.EMAIL}=?"
        val selectionArgs = arrayOf(email)

        val values = ContentValues().apply {
            put(DbHelper.PASSWORD, newPassword)
        }

        val rit = db.update(DbHelper.TABLE_NAME, values, selezione, selectionArgs)

        return rit
    }


    fun updateTelefonoUtente(email: String, nuovoNumero: String): Int {
        val selezione = "${DbHelper.EMAIL}=?"
        val selectionArgs = arrayOf(email)

        val values = ContentValues().apply {
            put(DbHelper.NUMEROCELLULARE, nuovoNumero)
        }

        val rit = db.update(DbHelper.TABLE_NAME, values, selezione, selectionArgs)

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