package org.example.webchatting.dto.login

import com.fasterxml.jackson.annotation.JsonProperty

class NaverLoginToken(
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("refresh_token") val refreshToken: String,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("expires_in") val expiresIn: String
)
