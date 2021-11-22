package com.perrofusion.peliseries.data.value_object

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
  val id: String,
  val name: String,
  val site: String,
  val key: String,
  val size: Int,
  val type: String
) : Parcelable
