package com.echcoding.carcaremanager

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform