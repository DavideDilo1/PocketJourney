package com.example.pocketjourney.model

import android.os.Parcel
import android.os.Parcelable

class HorizontalRecyclerItem(val itemId: Int, val imageUrl: Int, val nomeRistorante: String, val numRec: String, val valutazione: String, val stelle: Float, val testoVario: String ) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(itemId)
        parcel.writeInt(imageUrl)
        parcel.writeString(nomeRistorante)
        parcel.writeString(numRec)
        parcel.writeString(valutazione)
        parcel.writeFloat(stelle)
        parcel.writeString(testoVario)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HorizontalRecyclerItem> {
        override fun createFromParcel(parcel: Parcel): HorizontalRecyclerItem {
            return HorizontalRecyclerItem(parcel)
        }

        override fun newArray(size: Int): Array<HorizontalRecyclerItem?> {
            return arrayOfNulls(size)
        }
    }
}

