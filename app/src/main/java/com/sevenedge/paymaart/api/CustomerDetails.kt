//CustomerDetails.kt

package com.sevenedge.paymaart.api

import com.google.gson.annotations.SerializedName

data class CustomerDetails(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("data") val data: List<User>,
    @SerializedName("support") val support: SupportInfo
)


data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("avatar") val avatar: String
)


data class SupportInfo(
    @SerializedName("url") val url: String,
    @SerializedName("text") val text: String
)

