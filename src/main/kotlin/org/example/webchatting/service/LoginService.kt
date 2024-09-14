package org.example.webchatting.service

import jakarta.transaction.Transactional
import org.example.webchatting.dto.login.NaverLoginToken
import org.example.webchatting.dto.login.NaverUserInfoResponse
import org.example.webchatting.entity.UserEntity
import org.example.webchatting.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient


@Service
class LoginService(
    val userRepository: UserRepository,
    @Value("\${oauth.naver.clientId}") val clientId: String? = null,
    @Value("\${oauth.naver.clientSecret}") val clientSecret: String? = null,
    @Value("\${oauth.naver.redirectUrl}") val redirectUrl: String? = null,
    @Value("\${oauth.naver.csrfToken}") val csrfToken: String? = null
) {

    fun getLoginUrl(): String {
        val url: String = "https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +
                "&client_id=$clientId" +
                "&redirect_uri=$redirectUrl" +
                "&state=$csrfToken"

        return url
    }

    fun issueToken(code: String, state: String): String {
        if (state != csrfToken) {
            throw RuntimeException("invalid request")
        }

        val url: String = "https://nid.naver.com/oauth2.0/token?" +
                "grant_type=authorization_code" +
                "&client_id=$clientId" +
                "&client_secret=$clientSecret" +
                "&code=$code" +
                "&state=$csrfToken"

        val webClient: WebClient = WebClient.create(url)

        val naverLoginToken: NaverLoginToken = webClient.get()
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(NaverLoginToken::class.java)
            .block()
            ?: throw RuntimeException("Token is null")

        return naverLoginToken.accessToken
    }

    fun getUserInfoByToken(token: String): NaverUserInfoResponse {
        val apiURL = "https://openapi.naver.com/v1/nid/me"

        val webClient: WebClient = WebClient.create(apiURL)
        val response: NaverUserInfoResponse = webClient.get()
            .header("Authorization", "Bearer $token")
            .retrieve()
            .bodyToMono(NaverUserInfoResponse::class.java)
            .block()
            ?: throw RuntimeException("cannot find user info")

        return response
    }

    @Transactional
    fun findRegisteredId(userInfo: NaverUserInfoResponse): Long {
        val userEntity: UserEntity = userRepository.findByOauthId(userInfo.response.id)
            ?: UserEntity(
                oauthId = userInfo.response.id,
                name = userInfo.response.name
            )
        userRepository.save(userEntity)

        return userEntity.id
            ?: throw RuntimeException("cannot find user id")
    }
}