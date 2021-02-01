package com.example.turecallerdialog.model

import android.os.Parcelable
import androidx.databinding.ObservableBoolean
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "Contact")
@Parcelize
data class ContactModel(
    @PrimaryKey()
    var phoneNumber : String,
    var originalName : String? = null,
    var dummyName : String? =null,
   // var isChecked : ObservableBoolean = ObservableBoolean(false)
    var isChecked : Boolean? = false


) : Parcelable