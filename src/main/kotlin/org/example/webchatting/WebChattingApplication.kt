package org.example.webchatting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebChattingApplication

fun main(args: Array<String>) {
    runApplication<WebChattingApplication>(*args)
}
