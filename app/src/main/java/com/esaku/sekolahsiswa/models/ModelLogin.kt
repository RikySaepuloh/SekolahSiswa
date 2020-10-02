package com.saku.inventaris.models

import com.google.gson.annotations.SerializedName

data class ModelLogin(

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
)
