package com.example.edoctor.doctor

import com.google.gson.annotations.SerializedName

data class DoctorResponse(
    val id: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("secondName") val secondName: String,
    @SerializedName("thirdName") val thirdName: String,
    @SerializedName("spec") val specialization: String
)