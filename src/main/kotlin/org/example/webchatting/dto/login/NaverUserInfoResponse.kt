package org.example.webchatting.dto.login

class NaverUserInfoResponse(
    val resultcode: String,
    val message: String,
    val response: Response
) {
    class Response(
        val id: String,
        val name: String
    )
}