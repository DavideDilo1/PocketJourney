package com.example.pocketjourney.model

import android.os.Parcel
import android.os.Parcelable
import android.widget.RatingBar

class HomeItemModel(val image: Int, val title: String, val numRec: String, val valutazione: String, val stelle: Float ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readFloat()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(title)
        parcel.writeString(numRec)
        parcel.writeString(valutazione)
        parcel.writeFloat(stelle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HomeItemModel> {
        override fun createFromParcel(parcel: Parcel): HomeItemModel {
            return HomeItemModel(parcel)
        }

        override fun newArray(size: Int): Array<HomeItemModel?> {
            return arrayOfNulls(size)
        }
    }
}