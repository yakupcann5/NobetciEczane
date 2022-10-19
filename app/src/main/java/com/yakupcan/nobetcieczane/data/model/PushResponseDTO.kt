package com.yakupcan.nobetcieczane.data.model

import com.google.gson.annotations.SerializedName

open class PushResponseDTO(
    @SerializedName("message_id") var messageId : Long? = null

)