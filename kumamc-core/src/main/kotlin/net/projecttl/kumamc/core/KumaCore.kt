package net.projecttl.kumamc.core

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.velocity.VelocityProxy
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class KumaCore {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val server = MinecraftServer.init()
    private val secret = props().getProperty("velocity-secret")
    private var auth = props().getProperty("online-mode").toBoolean()
    private val addr = props().getProperty("host-ip")
    private val port = props().getProperty("host-port").toInt()

    private fun props(): Properties {
        val path = Path.of("server.properties")
        val prop = Properties()
        try {
            if (!Files.exists(path)) {
                val stream = javaClass.classLoader.getResourceAsStream("server.properties")
                prop.apply {
                    load(stream)
                    store(Files.newOutputStream(path), "KumaMC ${MinecraftServer.VERSION_NAME}")
                }

                return prop
            }

            prop.load(Files.newInputStream(path))
        } catch (ex: Exception) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }

        return prop
    }



    private fun initAuth() {
        when (props().getProperty("proxy-type")) {
            "none" -> {}
            "velocity" -> {
                if (secret == "") {
                    throw NullPointerException()
                }

                VelocityProxy.enable(secret)
                logger.info("[PROXY] enabled velocity forwarding")
                auth = false
            }
            "bungeecord" -> {
                BungeeCordProxy.enable()
                logger.info("[PROXY] enabled bungeecord forwarding")
                auth = false
            }
            else -> throw IllegalStateException()
        }

        if (auth) {
            MojangAuth.init()
            return
        }

        logger.warn("You're not enable online-mode")
    }

    suspend fun run() = coroutineScope {
        initAuth()

        launch {
            server.start(addr, port)
        }
    }
}