package com.example.androidchallenge.data.model

import android.os.Parcel
import android.os.Parcelable

data class LaunchDetail(
    val id: String?,
    var name: String?,
    val image: String?,
    var nameLaunchpad: String?,
    var region: String?,
    val success: Boolean,
    val upcoming: Boolean,
    val date: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
    }

    companion object CREATOR : Parcelable.Creator<LaunchDetail> {
        override fun createFromParcel(parcel: Parcel): LaunchDetail {
            return LaunchDetail(parcel)
        }

        override fun newArray(size: Int): Array<LaunchDetail?> {
            return arrayOfNulls(size)
        }
    }
}
