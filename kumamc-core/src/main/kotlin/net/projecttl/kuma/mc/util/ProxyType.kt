package net.projecttl.kuma.mc.util

fun proxyTypeChecker(name: String): ProxyType {
    return when (name) {
        ProxyType.NONE.str -> ProxyType.NONE
        ProxyType.BUNGEECORD.str -> ProxyType.BUNGEECORD
        ProxyType.VELOCITY.str -> ProxyType.VELOCITY
        else -> throw IllegalStateException()
    }
}

enum class ProxyType(val str: String) {
    NONE("none"),
    BUNGEECORD("bungeecord"),
    VELOCITY("velocity")
}