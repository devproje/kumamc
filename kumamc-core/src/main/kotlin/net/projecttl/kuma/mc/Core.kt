package net.projecttl.kuma.mc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.MojangAuth
import net.minestom.server.extras.bungee.BungeeCordProxy
import net.minestom.server.extras.velocity.VelocityProxy
import net.minestom.server.utils.NamespaceID
import net.projecttl.kuma.mc.handler.CampfireHandler
import net.projecttl.kuma.mc.handler.SignHandler
import net.projecttl.kuma.mc.handler.SkullHandler
import net.projecttl.kuma.mc.listeners.Listener
import net.projecttl.kuma.mc.api.utils.perm.model.PermData
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import java.util.UUID

private var configPath = Path.of("./server.properties")
var logger: ComponentLogger = MinecraftServer.LOGGER
lateinit var owner: UUID

suspend fun main() {
    val core = Core()
    core.registerHandler()
    core.databaseInit()
    core.modeInit()
    core.proxyInit()
    core.loadListener()
    core.ownerCheck()
    core.run()
}

class Core {
    private val server = MinecraftServer.init()
    private val handler = MinecraftServer.getGlobalEventHandler()

    fun props(): Properties {
        val props = Properties()
        try {
            if (Files.exists(configPath)) {
                props.load(Files.newInputStream(configPath))
                return props
            }

            val stream = javaClass.classLoader.getResourceAsStream("server.properties")
            props.apply {
                load(stream)
                store(Files.newOutputStream(configPath), "Minestom ${MinecraftServer.VERSION_NAME}")
            }
        } catch (ex: Exception) {
            MinecraftServer.getExceptionManager().handleException(ex)
        }

        return props
    }

    fun registerHandler() {
        MinecraftServer.getBlockManager().apply {
            registerHandler(NamespaceID.from(Key.key("minecraft:sign"))) { SignHandler() }
            registerHandler(NamespaceID.from(Key.key("minecraft:skull"))) { SkullHandler() }
            registerHandler(NamespaceID.from(Key.key("minecraft:campfire"))) { CampfireHandler() }
        }
    }

    fun databaseInit() {
        val dataDir = Path.of("./data")
        if (!Files.exists(dataDir)) {
            Files.createDirectory(dataDir)
        }

        val data = File("data", "data.db")
        if (!data.exists()) {
            data.createNewFile()
        }

        Database.connect("jdbc:sqlite:./${data.path}", driver = "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(PermData)
        }
    }

    fun modeInit() {
        try {
            when (props().getProperty("online-mode")) {
                "true" -> MojangAuth.init()
                "false" -> logger.warn("You're not enabled online-mode")
                else -> throw IllegalStateException()
            }

        } catch (e: Exception) {
            MinecraftServer.getExceptionManager().handleException(e)
        }
    }

    fun proxyInit() {
        val secret = props().getProperty("velocity-secret")
        when (props().getProperty("proxy-type")) {
            "none" -> {}

            "velocity" -> {
                if (secret == "") {
                    throw NullPointerException()
                }

                VelocityProxy.enable(secret)
                logger.info("Enabled velocity forward")
            }

            "bungeecord" -> {
                BungeeCordProxy.enable()
                logger.info("Enabled bungeecord forward")
            }

            else -> throw IllegalStateException()
        }
    }

    fun loadListener() {
        Listener.run(handler, this)
    }

    fun ownerCheck() {
        if (props().getProperty("owner-uuid") != null) {
            if (props().getProperty("owner-uuid") != "") {
                owner = UUID.fromString(props().getProperty("owner-uuid"))
            }
        }
    }

    suspend fun run() {
        coroutineScope {
            launch {
                val addr = props().getProperty("server-ip")
                val port = props().getProperty("server-port").toInt()

                logger.info("Minecraft ${MinecraftServer.VERSION_NAME}")
                logger.info("server binding port at: $port")
                server.start(addr, port)
            }
        }
    }
}