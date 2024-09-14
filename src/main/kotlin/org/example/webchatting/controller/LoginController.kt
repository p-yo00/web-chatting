package org.example.webchatting.controller

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.example.webchatting.dto.login.NaverUserInfoResponse
import org.example.webchatting.service.LoginService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class LoginController(val loginService: LoginService) {

    @GetMapping("login")
    fun login(): String = "login"

    @ResponseBody
    @GetMapping("login-url")
    fun getLoginUrl(): String {
        return loginService.getLoginUrl()
    }

    @GetMapping("login/redirect")
    fun loginSuccess(
        @RequestParam("code") code: String,
        @RequestParam("state") state: String,
        response: HttpServletResponse
    ): String {
        val token: String = loginService.issueToken(code, state)
        val userInfo: NaverUserInfoResponse = loginService.getUserInfoByToken(token)
        val userId: Long = loginService.findRegisteredId(userInfo)

        // cookie set
        val cookie = Cookie("userId", userId.toString())
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.maxAge = 3600
        response.addCookie(cookie)

        return "redirect:/chat/list"
    }

    @ResponseBody
    @GetMapping("cookie")
    fun checkCookie(@CookieValue("userId") userId: String): String {
        return userId
    }
}