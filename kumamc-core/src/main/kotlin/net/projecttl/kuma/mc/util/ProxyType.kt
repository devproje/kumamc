package net.projecttl.kuma.mc.util

fun proxyTypeChecker(name: String): ProxyType {
    return when (name) {
        ProxyType.NONE.name -> ProxyType.NONE
        ProxyType.BUNGEECORD.name -> ProxyType.BUNGEECORD
        ProxyType.VELOCITY.name -> ProxyType.VELOCITY
        else -> throw IllegalStateException()
    }
}

enum class ProxyType(name: String) {
    NONE("none"),
    BUNGEECORD("bungeecord"),
    VELOCITY("velocity")
}