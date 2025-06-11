package com.example.mygragment

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Entity(
    val species: String,
    val scientificName: String,
    val habitat: String,
    val diet: String,
    val conservationStatus: String,
    val averageLifespan: Int,
    val description: String
) : Parcelable