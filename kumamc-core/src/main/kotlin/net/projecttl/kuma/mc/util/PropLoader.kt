package net.projecttl.kuma.mc.util

import net.minestom.server.MinecraftServer
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import kotlin.properties.Delegates

data class Props(
    val onlineMode: Boolean,
    val proxyType: ProxyType,
    val secret: String,
    val world: String,
    val serverIp: String,
    val serverPort: Int
)

object PropLoader {
    private val prop = Properties()
    private val path = Path.of("server.properties")

    private fun loader(): Props = Props(
        onlineMode = prop.getProperty("online-mode").toBoolean(),
        proxyType = proxyTypeChecker(prop.getProperty("proxy-type")),
        secret = prop.getProperty("velocity-secret"),
        world = prop.getProperty("level-name"),
        serverIp = prop.getProperty("server-ip"),
        serverPort = prop.getProperty("server-port").toInt()
    )

    fun init(): Props {
        try {
            if (Files.exists(path)) {
                prop.load(Files.newInputStream(path))
                return loader()
            }

            val stream = javaClass.classLoader.getResourceAsStream("server.properties")
            prop.apply {
                load(stream)
                store(Files.newOutputStream(path), "Minestom ${MinecraftServer.VERSION_NAME}")
            }

        } catch (ex: Exception) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }

        return loader()
    }
}