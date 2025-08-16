package org.example.mandm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform