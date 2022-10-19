package com.yakupcan.nobetcieczane.domain.model

import com.google.gson.annotations.SerializedName

data class PushModel(
    @SerializedName("to") var to : String? = "",
    @SerializedName("priority") var priority : String? = "",
    @SerializedName("notification") var notification : Notification? = Notification(),
) {
}

data class Notification(
    @SerializedName("title") var title : String? = "",
    @SerializedName("body") var body : String? = "",
    @SerializedName("mutable_content") var mutableContent : Boolean? = false,
    @SerializedName("sound") var sound : String? = ""
)
