package net.projecttl.kuma.mc.util

import net.minestom.server.MinecraftServer
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import kotlin.properties.Delegates

object PropLoader {
    private val prop = Properties()
    private val path = Path.of("server.properties")

    var onlineMode by Delegates.notNull<Boolean>()
    lateinit var proxyType: ProxyType
    lateinit var secret: String
    lateinit var world: String

    lateinit var serverIp: String
    var serverPort by Delegates.notNull<Int>()

    private fun load() {
        onlineMode = prop.getProperty("online-mode").toBoolean()
        proxyType = proxyTypeChecker(prop.getProperty("proxy-type"))
        secret = prop.getProperty("velocity-secret")
        world = prop.getProperty("level-name")

        serverIp = prop.getProperty("server-ip")
        serverPort = prop.getProperty("server-port").toInt()
    }

    fun init() {
        try {
            if (Files.exists(path)) {
                prop.load(Files.newInputStream(path))
                load()
                return
            }

            val stream = javaClass.classLoader.getResourceAsStream("server.properties")
            prop.apply {
                load(stream)
                store(Files.newOutputStream(path), "Minestom ${MinecraftServer.VERSION_NAME}")
            }

            load()
        } catch (ex: Exception) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }
    }
}