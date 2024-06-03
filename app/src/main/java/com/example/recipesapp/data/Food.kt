package com.example.recipesapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Food(

    val url: String? = null,
    val foodName: String? = null,
    val Description: String? = null,
    val link: String? = null,
    val source: String? = null,
   // val sourceYoutube: String? = null

) : Parcelable {

    constructor():this(
        " " ,
        " ",
        "",
        " "

    )

}